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
 * @author ejmc
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
