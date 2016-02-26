/**
 * Copyright (C) 2016 emenaceb (emenaceb@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.emenaceb.appjar.boot;

import static com.github.emenaceb.appjar.boot.MagicAppJarBoot.LIB_PREFIX;
import static com.github.emenaceb.appjar.boot.MagicAppJarBoot.MAVEN_APPJAR_BOOT_ARTIFACT_ID;
import static com.github.emenaceb.appjar.boot.MagicAppJarBoot.MAVEN_APPJAR_GROUP_ID;
import static com.github.emenaceb.appjar.boot.MagicAppJarBoot.MF_APPJAR_MAIN_CLASS;
import static com.github.emenaceb.appjar.boot.MagicAppJarBoot.SP_APPJAR_NO_BANNER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.emenaceb.appjar.boot.apploader.Handler;

/**
 * AppJar boot class.
 * 
 * @author emenaceb
 *
 */
public class AppJarBoot {

	private static final String BOOT_CLASS_FILE_NAME = AppJarBoot.class.getName().replace('.', '/') + ".class";

	private final static Pattern LIB_PATTERN = Pattern.compile("^" + LIB_PREFIX + "([^/]+)/$");

	private static final String MAVEN_META_INF = "META-INF/maven/";

	private static final String MAVEN_POM_PROPERTIES = "/pom.properties";

	public final static String SP_JAVA_PROTOCOL_HANDLER = "java.protocol.handler.pkgs";

	public final static String SP_JAVA_CLASS_PATH = "java.class.path";

	public static void main(String[] args) throws Exception {
		new AppJarBoot().run(args);
	}

	private void abort(String message) {
		System.out.println(message);
		System.exit(255);
	}

	private void executeMainClass(AppJarInfo info, String[] args) throws Exception {

		ClassLoader classLoader = info.getClassLoader();

		Class<?> c = classLoader.loadClass(info.getMainClass());

		Method m = c.getMethod("main", String[].class);
		if (m == null) {
			abort("Class " + c.getName() + " has no main() method");
		}

		int modifiers = m.getModifiers();
		if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
			abort("Class " + c.getName() + " main() method has to be public and static");
		}
		m.invoke(null, new Object[] { args });
	}

	public void extractLibraries(AppJarInfo info) throws IOException {
		JarFile jar = info.getJarFile();

		Matcher matcher = LIB_PATTERN.matcher("");

		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry != null) {
				String name = entry.getName();
				if (entry.isDirectory()) {
					matcher.reset(name);
					if (matcher.matches()) {
						URL url = new URL(Handler.PROTOCOL, matcher.group(1), "/");
						info.addLibrary(url);
					}

				}
			}

		}

	}

	public void extractMainClass(AppJarInfo info) throws IOException {
		Manifest manifest = info.getJarFile().getManifest();
		String mainClass = manifest.getMainAttributes().getValue(MF_APPJAR_MAIN_CLASS);
		if (mainClass != null) {
			String trimmed = mainClass.trim();
			if (trimmed.length() != 0) {
				info.setMainClass(trimmed);
			}
		}
		if (info.getMainClass() == null) {
			abort("Unable to find " + MF_APPJAR_MAIN_CLASS + " in manifest");
		}
	}

	public void extractVersion(AppJarInfo info) throws IOException {

		String resource = MAVEN_META_INF + MAVEN_APPJAR_GROUP_ID + "/" + MAVEN_APPJAR_BOOT_ARTIFACT_ID + MAVEN_POM_PROPERTIES;
		InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
		if (is == null) {
			abort("Not an AppJar application");
		}
		try {
			Properties pom = new Properties();
			pom.load(is);

			String version = pom.getProperty("version");
			if (version == null || version.trim().length() == 0) {
				version = "???";
			} else {
				version = version.trim();
			}
			info.setVersion(version);
		} finally {
			is.close();
		}

	}

	public File findAppJarJar() {

		String classPath = System.getProperty(SP_JAVA_CLASS_PATH);
		String classPathParts[] = classPath.split(File.pathSeparator);

		// Check files
		for (String classPathPart : classPathParts) {

			File candidateJarFile = new File(classPathPart);
			if (candidateJarFile.exists() && candidateJarFile.isFile() && candidateJarFile.canRead()) {
				if (isAppJarJar(candidateJarFile)) {
					return candidateJarFile;
				}
			}

		}

		return null;
	}

	private boolean isAppJarJar(File candidateJarFile) {
		boolean found = false;

		JarFile jar = null;
		try {
			jar = new JarFile(candidateJarFile);
			JarEntry entry = jar.getJarEntry(BOOT_CLASS_FILE_NAME);
			found = entry != null;
		} catch (IOException ex) {

			// Ignore
		} finally {
			if (jar != null) {
				try {
					jar.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return found;
	}

	private void openJar(AppJarInfo info) throws IOException {

		File file = findAppJarJar();
		if (file == null) {
			abort("Unable to find running Jar file ");
		}

		final JarFile jarFile = new JarFile(file);
		info.setJarFile(jarFile);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					jarFile.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private void printBanner(AppJarInfo info) {
		String version = info.getVersion();

		System.out.println();
		if (System.getProperty(SP_APPJAR_NO_BANNER) == null) {

			System.out.println("   ___                  __        ");
			System.out.println("  / _ | ___  ___    __ / /__ _____      v " + version);
			System.out.println(" / __ |/ _ \\/ _ \\  / // / _ `/ __/      ");
			System.out.println("/_/ |_/ .__/ .__/  \\___/\\_,_/_/         by emenaceb 2016");
			System.out.println("     /_/  /_/                             ");

			System.out.println();

		} else {
			System.out.println(" AppJar v " + version + "  by emenaceb 2016");
		}
		System.out.println("----------------------------------------------------------------------");

	}

	private void registerClassLoader(AppJarInfo info) {
		URL[] urls = info.getLibraryURLs().toArray(new URL[info.getLibraryURLs().size()]);
		URLClassLoader classLoader = new URLClassLoader(urls, getClass().getClassLoader());
		info.setClassLoader(classLoader);
		Thread.currentThread().setContextClassLoader(classLoader);

	}

	private void registerProtocol() {

		String handlerPackage = System.getProperty(SP_JAVA_PROTOCOL_HANDLER);
		if (handlerPackage == null) {
			handlerPackage = "";
		} else if (handlerPackage.length() > 0) {
			handlerPackage = "|" + handlerPackage;
		}
		handlerPackage = getClass().getPackage().getName() + handlerPackage;
		System.setProperty(SP_JAVA_PROTOCOL_HANDLER, handlerPackage);
	}

	public void run(String[] args) throws Exception {

		AppJarInfo info = AppJarInfo.getInstance();

		extractVersion(info);

		printBanner(info);

		registerProtocol();

		openJar(info);

		extractMainClass(info);

		extractLibraries(info);

		registerClassLoader(info);

		System.out.println();

		executeMainClass(info, args);

	}
}
