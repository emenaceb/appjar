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

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.github.emenaceb.appjar.maven.executor.AddBootExecutor;
import com.github.emenaceb.appjar.maven.executor.AssemblyExecutor;
import com.github.emenaceb.appjar.maven.executor.ExecutorContext;
import com.github.emenaceb.appjar.maven.executor.GoalDescriptor;
import com.github.emenaceb.appjar.maven.executor.UnpackDependenciesExecutor;
import com.github.emenaceb.appjar.maven.utils.PluginUtils;

/**
 * Packages an application as an AppJar.<br>
 * <br>
 * Packages a existing java application into a jar with all it's dependencies.
 * <br>
 * This goal is designed to be added to an existing project to produce a new
 * artifact in addition to the main artifact.
 * 
 * @author ejmc
 *
 */
@Mojo(name = "simple", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class SimpleMojo extends AbstractMojo {

	public static final GoalDescriptor JAR = new GoalDescriptor("org.apache.maven.plugins", "maven-jar-plugin", "2.0", "jar");

	@Parameter(defaultValue = "${session}", readonly = true)
	private MavenSession session;

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	@Parameter(defaultValue = "${mojoExecution}", readonly = true)
	private MojoExecution mojo;

	@Parameter(defaultValue = "${plugin}", readonly = true) // Maven 3 only
	private PluginDescriptor plugin;

	@Component
	private BuildPluginManager pluginManager;

	@Component
	private MavenProjectHelper projectHelper;

	@Parameter(defaultValue = "${project.build.directory}", readonly = true)
	private File target;

	/**
	 * Classifier for the generated artifact.<br>
	 * By default it uses app as classifier.
	 */
	@Parameter(readonly = false, required = false)
	private String alternateClassifier;

	/**
	 * Class to be executed by the AppJar Bootstrap.<br>
	 * <br>
	 * Equivalent to the Main-Class attribute in the manifest.
	 */
	@Parameter(readonly = false, required = false)
	private String mainClass;

	/**
	 * Final name for the artifact.
	 */
	@Parameter(defaultValue = "${project.build.finalName}", readonly = false, required = true)
	private String finalName;

	public void execute() throws MojoExecutionException, MojoFailureException {

		ExecutorContext ctx = new ExecutorContext(plugin, project, session, pluginManager, projectHelper);

		getLog().info("");
		getLog().info("Adding bootstrap");
		getLog().info("");
		AddBootExecutor addBoot = new AddBootExecutor(ctx);
		addBoot.exec();

		getLog().info("");
		getLog().info("Unpacking dependencies");
		getLog().info("");
		UnpackDependenciesExecutor dependencies = new UnpackDependenciesExecutor(ctx);
		dependencies.exec();

		getLog().info("");
		getLog().info("Packaging application");
		getLog().info("");

		String effectiveMainClass = resolveMainClass(ctx);
		AssemblyExecutor assemblyExecutor = new AssemblyExecutor(ctx, finalName, effectiveMainClass, alternateClassifier);

		assemblyExecutor.exec();

	}

	private String resolveMainClass(ExecutorContext ctx) throws MojoExecutionException {
		Plugin p = PluginUtils.resolveProjectPlugin(ctx, JAR);
		if (p != null) {
			Xpp3Dom projectConfig = (Xpp3Dom) p.getConfiguration();
			if (projectConfig != null) {
				Xpp3Dom arch = projectConfig.getChild("archive");
				if (arch != null) {
					Xpp3Dom mf = arch.getChild("manifest");
					if (mf != null) {
						Xpp3Dom mc = mf.getChild("mainClass");
						if (mc != null) {
							String tmp = mc.getValue();
							if (StringUtils.isNotBlank(tmp)) {
								return tmp.trim();
							}
						}
					}
				}
			}
		}
		if (mainClass == null) {
			throw new MojoExecutionException("Can't find main class");
		}
		return mainClass;
	}

}
