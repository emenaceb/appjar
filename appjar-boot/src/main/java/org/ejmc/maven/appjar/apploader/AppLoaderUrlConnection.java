package org.ejmc.maven.appjar.apploader;

import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class AppLoaderUrlConnection extends URLConnection {

	private JarFile file;

	AppLoaderUrlConnection(URL url, JarFile file) {
		super(url);
		this.file = file;
	}

	@Override
	public void connect() {
	}

	@Override
	public String getContentType() {
		FileNameMap fileNameMap = java.net.URLConnection.getFileNameMap();
		String contentType = fileNameMap.getContentTypeFor(url.getPath());
		if (contentType == null)
			contentType = "text/plain";
		return contentType;
	}

	public boolean exists() {
		String path = getInnerResource(getURL());
		return file.getJarEntry(path) != null;
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

	private String getInnerResource(URL u) {
		String innerJar = u.getHost();
		innerJar = "lib/" + innerJar;
		String relativePath = u.getPath();
		String path = innerJar + relativePath;
		return path;
	}

}