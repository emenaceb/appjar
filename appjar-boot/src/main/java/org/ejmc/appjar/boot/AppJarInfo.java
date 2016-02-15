package org.ejmc.appjar.boot;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * Holds App information.
 * 
 * @author ejmc
 *
 */
public class AppJarInfo {

	private static AppJarInfo instance = new AppJarInfo();

	public static AppJarInfo getInstance() {

		return instance;
	}

	private JarFile jarFile;

	private List<URL> libs = new ArrayList<URL>();

	private String mainClass;

	private URLClassLoader classLoader;

	public void addLibrary(URL url) {
		this.libs.add(url);
	}

	public URLClassLoader getClassLoader() {
		return classLoader;
	}

	public JarFile getJarFile() {
		return jarFile;
	}

	public List<URL> getLibraryURLs() {
		return libs;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setClassLoader(URLClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setJarFile(JarFile jarFile) {
		this.jarFile = jarFile;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

}
