package com.github.emenaceb.appjar.maven.executor;

import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;

import org.apache.maven.plugin.MojoExecutionException;

import com.github.emenaceb.appjar.maven.MagicAppJarPlugin;

/**
 * Adds boot to app jar.
 * 
 * @author ejmc
 *
 */
public class AddBootExecutor extends BaseMojoExecutor {

	public static final GoalDescriptor GOAL = new GoalDescriptor("org.apache.maven.plugins", "maven-dependency-plugin", "2.10", "unpack");

	public AddBootExecutor(ExecutorContext context) {
		super(context);
	}

	@Override
	public void exec() throws MojoExecutionException {

		execMojo(GOAL, //
				configuration(//
						element("artifactItems", //
								element("artifactItem", //
										element("groupId", context.getPlugin().getGroupId()), //
										element("artifactId", MagicAppJarPlugin.APPJAR_BOOT_ARTIFACT_ID), //
										element("version", context.getPlugin().getVersion()), //
										element("outputDirectory", MagicAppJarPlugin.APPJAR_BUILD_DIR)))//
		));
	}
}
