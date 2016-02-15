package org.ejmc.appjar.boot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.ejmc.appjar.boot.apploader.Handler;

/**
 * AppJar boot class.
 * 
 * @author ejmc
 *
 */
public class AppJarBoot {

	private static final String BASE_PACKAGE = "org.ejmc.appjar";

	private static final String MF_FAT_JAR_MAIN_CLASS = "FatJar-Main-Class";

	private final static Pattern LIB_PATTERN = Pattern.compile("^lib/([^/]+)/$");

	public final static String SP_JAVA_PROTOCOL_HANDLER = "java.protocol.handler.pkgs";

	public final static String SP_JAVA_CLASS_PATH = "java.class.path";

	public static void main(String[] args) throws Exception {

		new AppJarBoot().run(args);

	}

	private void createClassLoader(AppJarInfo info) {
		URL[] urls = info.getLibraryURLs().toArray(new URL[info.getLibraryURLs().size()]);

		URLClassLoader classLoader = new URLClassLoader(urls, getClass().getClassLoader());
		info.setClassLoader(classLoader);

	}

	public void extractLibraries(AppJarInfo info) throws IOException {
		JarFile jar = info.getJarFile();

		Matcher matcher = LIB_PATTERN.matcher("");

		Enumeration<JarEntry> entries = jar.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry == null) {
				continue;
			}

			String name = entry.getName();
			if (entry.isDirectory()) {
				matcher.reset(name);
				if (matcher.matches()) {
					URL url = new URL(Handler.PROTOCOL, matcher.group(1), "/");
					// System.out.println(url);
					info.addLibrary(url);
				}

			}

		}

	}

	public void extractMainClass(AppJarInfo info) throws IOException {
		Manifest manifest = info.getJarFile().getManifest();
		String mainClass = manifest.getMainAttributes().getValue(MF_FAT_JAR_MAIN_CLASS);
		if (mainClass == null || mainClass.trim().length() == 0) {
			throw new IOException(MF_FAT_JAR_MAIN_CLASS + " no presente o vacio ");
		}
		info.setMainClass(mainClass);
	}

	public JarEntry findJarEntry(JarInputStream jis, String name) throws IOException {
		JarEntry entry;
		while ((entry = jis.getNextJarEntry()) != null) {
			if (entry.getName().equals(name)) {
				return entry;
			}
		}
		return null;
	}

	public ZipEntry findZipEntry(ZipFile zip, String name) throws IOException {
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entry.getName().equals(name))
				return entry;
		}
		return null;
	}

	public File getMyJarFile() {

		String myJarPath = null;
		try {
			// Hack to obtain the name of this jar file.
			String jarname = System.getProperty(SP_JAVA_CLASS_PATH);
			// Open each Jar file looking for this class name. This allows
			// for
			// JVM's that place more than the jar file on the classpath.
			String jars[] = jarname.split(System.getProperty("path.separator"));
			for (int i = 0; i < jars.length; i++) {
				jarname = jars[i];
				System.out.println("Checking " + jarname + " as Fat-Jar file");
				// Allow for URL based paths, as well as file-based paths.
				// File
				InputStream is = null;
				try {
					is = new URL(jarname).openStream();
				} catch (MalformedURLException mux) {
					// Try a local file.
					try {
						is = new FileInputStream(jarname);
					} catch (IOException iox) {
						// Ignore..., but it isn't good to have bad entries
						// on the classpath.
						continue;
					}
				}
				ZipEntry entry = findJarEntry(new JarInputStream(is), AppJarBoot.class.getName().replace('.', '/') + ".class");
				if (entry != null) {
					myJarPath = jarname;
					break;
				} else {
					// One more try as a Zip file: supports launch4j on
					// Windows.
					entry = findZipEntry(new ZipFile(jarname), AppJarBoot.class.getName().replace('.', '/') + ".class");
					if (entry != null) {
						myJarPath = jarname;
						break;
					}
				}
			}
		} catch (Exception x) {
			x.printStackTrace();
			System.out.println("jar=" + myJarPath + " loaded from " + SP_JAVA_CLASS_PATH + " (" + System.getProperty(SP_JAVA_CLASS_PATH) + ")");
		}

		if (myJarPath == null) {
			throw new IllegalArgumentException("Unable to locate " + getClass().getName() + " in the java.class.path");
		}
		// Normalize those annoying DOS backslashes.
		myJarPath = myJarPath.replace('\\', '/');

		File jar = new File(myJarPath);
		if (!jar.exists() || !jar.canRead()) {
			throw new IllegalArgumentException("Unable to open " + jar);
		}

		// System.out.println("Found " + jar.getAbsolutePath());
		return jar;
	}

	private void openJar(AppJarInfo info) throws IOException {
		File file = getMyJarFile();
		final JarFile jarFile = new JarFile(file);
		info.setJarFile(jarFile);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					jarFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void registerProtocol() {

		String handlerPackage = System.getProperty(SP_JAVA_PROTOCOL_HANDLER);
		if (handlerPackage == null) {
			handlerPackage = "";
		} else if (handlerPackage.length() > 0) {
			handlerPackage = "|" + handlerPackage;
		}
		handlerPackage = BASE_PACKAGE + handlerPackage;
		System.setProperty(SP_JAVA_PROTOCOL_HANDLER, handlerPackage);
	}

	public void run(String[] args) throws Exception {

		registerProtocol();

		AppJarInfo info = AppJarInfo.getInstance();

		openJar(info);

		extractMainClass(info);

		extractLibraries(info);

		createClassLoader(info);

		runMainClass(info, args);

	}

	private void runMainClass(AppJarInfo info, String[] args) throws Exception {

		ClassLoader classLoader = info.getClassLoader();
		Thread.currentThread().setContextClassLoader(classLoader);
		Class<?> c = classLoader.loadClass(info.getMainClass());

		Method m = c.getMethod("main", String[].class);

		Object main = c.newInstance();
		m.invoke(main, new Object[] { args });
	}
}
