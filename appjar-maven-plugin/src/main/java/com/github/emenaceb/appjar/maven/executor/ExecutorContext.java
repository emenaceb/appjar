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
