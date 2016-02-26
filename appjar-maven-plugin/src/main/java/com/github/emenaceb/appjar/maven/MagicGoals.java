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
package com.github.emenaceb.appjar.maven;

import com.github.emenaceb.appjar.maven.executor.GoalDescriptor;

/**
 * Required plugins and goals and default versions.
 * 
 * @author emenaceb
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
