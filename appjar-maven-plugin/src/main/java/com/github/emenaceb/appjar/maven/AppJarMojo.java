package com.github.emenaceb.appjar.maven;

import java.io.File;

import org.apache.maven.execution.MavenSession;
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

import com.github.emenaceb.appjar.maven.executor.AddBootExecutor;
import com.github.emenaceb.appjar.maven.executor.AssemblyExecutor;
import com.github.emenaceb.appjar.maven.executor.ExecutorContext;
import com.github.emenaceb.appjar.maven.executor.UnpackDependenciesExecutor;

/**
 * Pacakges an application as an AppJar.
 * 
 * @author ejmc
 *
 */
@Mojo(name = "appjar", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class AppJarMojo extends AbstractMojo {

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

	@Parameter(defaultValue = "${project.build.directory}", readonly = true)
	private File target;

	@Parameter(defaultValue = "app", readonly = false, required = true)
	private String outputClassifier;

	@Parameter(readonly = false, required = true)
	private String mainClass;

	public void execute() throws MojoExecutionException, MojoFailureException {

		ExecutorContext ctx = new ExecutorContext(plugin, project, session, pluginManager);

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
		AssemblyExecutor assemblyExecutor = new AssemblyExecutor(ctx, mainClass);

		assemblyExecutor.exec();

	}

}
