package com.github.emenaceb.appjar.maven.executor;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;

public class ExecutorContext {

	public ExecutorContext(PluginDescriptor plugin, MavenProject project, MavenSession session, BuildPluginManager pluginManager) {
		super();
		this.plugin = plugin;
		this.project = project;
		this.session = session;
		this.pluginManager = pluginManager;
	}

	private MavenSession session;

	private MavenProject project;

	private PluginDescriptor plugin;

	private BuildPluginManager pluginManager;

	public MavenSession getSession() {
		return session;
	}

	public MavenProject getProject() {
		return project;
	}

	public PluginDescriptor getPlugin() {
		return plugin;
	}

	public BuildPluginManager getPluginManager() {
		return pluginManager;
	}

}
