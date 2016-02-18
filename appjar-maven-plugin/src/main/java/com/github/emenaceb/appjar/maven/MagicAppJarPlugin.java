package com.github.emenaceb.appjar.maven;

/**
 * Constant class for plugin
 * 
 * @author ejmc
 *
 */
public final class MagicAppJarPlugin {

	public static final String APPJAR_BUILD_DIR = "${project.build.directory}/appjar";
	public static final String APPJAR_BOOT_ARTIFACT_ID = "appjar-boot";

	private MagicAppJarPlugin() {
	}
}
