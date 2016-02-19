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
package com.github.emenaceb.appjar.maven.executor;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

/**
 * Plugin execution context.
 * 
 * @author ejmc
 *
 */
public class ExecutorContext {

	private MavenSession session;

	private MavenProject project;

	private PluginDescriptor plugin;

	private BuildPluginManager pluginManager;

	private MavenProjectHelper projectHelper;

	public ExecutorContext(PluginDescriptor plugin, MavenProject project, MavenSession session, BuildPluginManager pluginManager, MavenProjectHelper projectHelper) {
		super();
		this.plugin = plugin;
		this.project = project;
		this.session = session;
		this.pluginManager = pluginManager;
		this.projectHelper = projectHelper;
	}

	public PluginDescriptor getPlugin() {
		return plugin;
	}

	public BuildPluginManager getPluginManager() {
		return pluginManager;
	}

	public MavenProject getProject() {
		return project;
	}

	public MavenProjectHelper getProjectHelper() {
		return projectHelper;
	}

	public MavenSession getSession() {
		return session;
	}

}
