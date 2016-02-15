package org.ejmc.appjar.boot.apploader;

import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * UrlConnection to load appjar resources.
 * 
 * @author ejmc
 *
 */
public final class AppLoaderUrlConnection extends URLConnection {

	private JarFile file;

	public AppLoaderUrlConnection(URL url, JarFile file) {
		super(url);
		this.file = file;
	}

	@Override
	public void connect() {
	}

	public boolean exists() {
		String path = getInnerResource(getURL());
		return file.getJarEntry(path) != null;
	}

	@Override
	public String getContentType() {
		FileNameMap fileNameMap = java.net.URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor(url.getPath());
		if (contentType == null)
			contentType = "text/plain";
		return contentType;
	}

	private String getInnerResource(URL u) {
		String innerJar = u.getHost();
		innerJar = "lib/" + innerJar;
		String relativePath = u.getPath();
		String path = innerJar + relativePath;
		return path;
	}

	@Override
	public InputStream getInputStream() throws IOException {

		String path = getInnerResource(getURL());

		InputStream is = null;
		JarEntry entry = file.getJarEntry(path);
		if (entry != null) {
			is = file.getInputStream(entry);
		}
		if (is == null)
			throw new IOException("cl.getByteStream() returned null for " + getURL().getPath());
		return is;

	}

}