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

import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.twdata.maven.mojoexecutor.MojoExecutor;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;

/**
 * Base class for other plugin executions.
 * 
 * @author ejmc
 *
 */
public abstract class BaseMojoExecutor {

	protected ExecutorContext context;

	public BaseMojoExecutor(ExecutorContext context) {
		super();
		this.context = context;
	}

	protected Dependency dependency(String groupId, String artifactId, String version) {
		Dependency dependency = new Dependency();
		dependency.setGroupId(groupId);
		dependency.setArtifactId(artifactId);
		dependency.setVersion(version);
		return dependency;

	}

	protected Element[] elementArray(List<Element> elements) {
		return elements.toArray(new Element[elements.size()]);
	}

	protected void execMojo(GoalDescriptor goal, Xpp3Dom configuration) throws MojoExecutionException {
		execMojo(goal, configuration, null);
	}

	protected void execMojo(GoalDescriptor goal, Xpp3Dom configuration, List<Dependency> dependencies) throws MojoExecutionException {
		Plugin plugin = resolvePlugin(goal, dependencies);
		MojoExecutor.executeMojo(//
				plugin, //
				goal.getGoal(), //
				configuration, //
				executionEnvironment(context.getProject(), context.getSession(), context.getPluginManager()));
	}

	public abstract void exec() throws MojoExecutionException;

	private Plugin resolvePlugin(GoalDescriptor goal, List<Dependency> dependencies) {
		String version = goal.getDefaultVersion();
		Plugin p = context.getProject().getPlugin(goal.getGroupId() + ":" + goal.getArtifactId());
		if (p != null) {
			version = p.getVersion();
		}
		if (StringUtils.isBlank(version)) {
			version = goal.getDefaultVersion();
		}
		List<Dependency> deps = dependencies;
		if (deps == null) {
			deps = Collections.emptyList();
		}
		return plugin(goal.getGroupId(), goal.getArtifactId(), version, deps);
	}

	protected List<Dependency> singleDependency(String groupId, String artifactId, String version) {
		return Collections.singletonList(dependency(groupId, artifactId, version));
	}

}
