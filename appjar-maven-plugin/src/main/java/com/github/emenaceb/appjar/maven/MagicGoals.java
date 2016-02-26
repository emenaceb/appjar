package com.github.emenaceb.appjar.maven;

import com.github.emenaceb.appjar.maven.executor.GoalDescriptor;

/**
 * Required plugins and goals and default versions.
 * 
 * @author ejmc
 *
 */
public final class MagicGoals {

	public static final GoalDescriptor ASSEMBLY_SINGLE = new GoalDescriptor("org.apache.maven.plugins", "maven-assembly-plugin", "2.6", "single");

	public static final GoalDescriptor DEPENDENCIES_UNPACK = new GoalDescriptor("org.apache.maven.plugins", "maven-dependency-plugin", "2.10", "unpack");

	public static final GoalDescriptor DEPENDENCIES_UNPACK_DEPS = new GoalDescriptor("org.apache.maven.plugins", "maven-dependency-plugin", "2.10", "unpack-dependencies");

	public static final GoalDescriptor JAR_JAR = new GoalDescriptor("org.apache.maven.plugins", "maven-jar-plugin", "2.0", "jar");

	private MagicGoals() {

	}
}
