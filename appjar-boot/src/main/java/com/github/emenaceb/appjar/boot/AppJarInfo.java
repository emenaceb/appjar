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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * Holds AppJar information.
 * 
 * @author ejmc
 *
 */
public class AppJarInfo {

	private static AppJarInfo instance = new AppJarInfo();

	public static AppJarInfo getInstance() {
		return instance;
	}

	private String version;

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

	public String getVersion() {
		return version;
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

	public void setVersion(String version) {
		this.version = version;
	}

}
