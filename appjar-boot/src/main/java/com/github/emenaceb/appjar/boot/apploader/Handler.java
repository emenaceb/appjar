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

	/* remove host lookup */
	@Override
	protected int hashCode(URL u) {
		int h = 0;

		// Generate the protocol part.
		String protocol = u.getProtocol();
		if (protocol != null) {
			h += protocol.hashCode();
		}

		// Generate the host part.
		String host = u.getHost();
		if (host != null) {
			h += host.toLowerCase().hashCode();
		}

		// Generate the file part.
		String file = u.getFile();
		if (file != null) {
			h += file.hashCode();
		}

		// Generate the port part.
		if (u.getPort() == -1) {
			h += getDefaultPort();
		} else {
			h += u.getPort();
		}

		// Generate the ref part.
		String ref = u.getRef();
		if (ref != null) {
			h += ref.hashCode();
		}

		return h;
	}

	/* remove host lookup */
	@Override
	protected boolean hostsEqual(URL u1, URL u2) {
		String h1 = u1 != null ? u1.getHost() : null;
		String h2 = u2 != null ? u2.getHost() : null;
		// else, if both have host names, compare them
		if (h1 != null && h2 != null) {
			return h1.equalsIgnoreCase(h2);
		} else {
			return h1 == null && h2 == null;
		}
	}

}
