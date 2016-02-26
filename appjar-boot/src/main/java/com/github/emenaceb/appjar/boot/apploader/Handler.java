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
package com.github.emenaceb.appjar.boot.apploader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import com.github.emenaceb.appjar.boot.AppJarInfo;

/**
 * Handles apploader protocol.
 * 
 * @author emenaceb
 * 
 */
public class Handler extends URLStreamHandler {

	public static String PROTOCOL = "apploader";

	/**
	 * @see java.net.URLStreamHandler#openConnection(java.net.URL)
	 */
	@Override
	protected URLConnection openConnection(final URL u) throws IOException {

		AppLoaderUrlConnection conn = new AppLoaderUrlConnection(u, AppJarInfo.getInstance().getJarFile());
		if (conn.exists()) {
			return conn;
		}
		throw new FileNotFoundException(u.toExternalForm());
	}

}
