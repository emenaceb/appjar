package com.github.emenaceb.appjar.maven.executor;

import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;

import com.github.emenaceb.appjar.boot.AppJarBoot;
import com.github.emenaceb.appjar.boot.MagicAppJarBoot;

/**
 * Packs app jar.
 * 
 * @author ejmc
 *
 */
public class AssemblyExecutor extends BaseMojoExecutor {

	public static final GoalDescriptor GOAL = new GoalDescriptor("org.apache.maven.plugins", "maven-assembly-plugin", "2.4", "single");

	private String mainClass;

	public AssemblyExecutor(ExecutorContext context, String mainClass) {
		super(context);
		this.mainClass = mainClass;
	}

	@Override
	public void exec() throws MojoExecutionException {

		packageWithAssembly("appjar", //
				element("archive", //
						element("addMavenDescriptor", "true"), //
						element("manifest", //
								element("mainClass", AppJarBoot.class.getName()), //
								element("addDefaultImplementationEntries", "true"), //
								element("addDefaultSpecificationEntries", "true")), //
						element("manifestEntries", //
								element(MagicAppJarBoot.MF_APPJAR_MAIN_CLASS, mainClass))//
		));

	}

	protected void packageWithAssembly(String ref, Element... elements) throws MojoExecutionException {

		List<Element> config = new ArrayList<Element>();
		config.add(element("descriptorRefs", element("descriptorRef", ref)));
		if (elements != null) {
			config.addAll(Arrays.asList(elements));
		}

		PluginDescriptor currentPlugin = context.getPlugin();
		execMojo(GOAL, //
				configuration(elementArray(config)), //
				singleDependency(currentPlugin.getGroupId(), currentPlugin.getArtifactId(), currentPlugin.getVersion()));

	}

}
