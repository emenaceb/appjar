package com.github.emenaceb.appjar.maven.executor;

import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;

import org.apache.maven.plugin.MojoExecutionException;

import com.github.emenaceb.appjar.boot.MagicAppJarBoot;
import com.github.emenaceb.appjar.maven.MagicAppJarPlugin;

/**
 * Adds dependencies to app jar.
 * 
 * @author ejmc
 *
 */
public class UnpackDependenciesExecutor extends BaseMojoExecutor {

	public static final GoalDescriptor GOAL = new GoalDescriptor("org.apache.maven.plugins", "maven-dependency-plugin", "2.10", "unpack-dependencies");

	public UnpackDependenciesExecutor(ExecutorContext context) {
		super(context);
	}

	@Override
	public void exec() throws MojoExecutionException {

		execMojo(GOAL, //
				configuration(//
						element("outputDirectory", MagicAppJarPlugin.APPJAR_BUILD_DIR + "/" + MagicAppJarBoot.LIB_PREFIX), //
						element("useSubDirectoryPerArtifact", "true")));
	}
}
