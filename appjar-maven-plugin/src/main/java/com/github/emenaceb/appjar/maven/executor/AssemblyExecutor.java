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

import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.twdata.maven.mojoexecutor.MojoExecutor.Element;

import com.github.emenaceb.appjar.boot.AppJarBoot;
import com.github.emenaceb.appjar.boot.MagicAppJarBoot;
import com.github.emenaceb.appjar.maven.MagicGoals;

/**
 * Packs app jar.
 * 
 * @author ejmc
 *
 */
public class AssemblyExecutor extends BaseMojoExecutor {

	private static final String APPJAR_ASSEMBLY_NAME = "appjar";
	private static final String APPJAR_ASSEMBLY_ID = "app";
	private static final String APPJAR_ASSEMBLY_EXT = "jar";

	private String mainClass;

	private String finalName;

	private String alternateClassifier;

	public AssemblyExecutor(ExecutorContext context, String finalName, String mainClass, String alternateClassifier) {
		super(context);
		this.mainClass = mainClass;
		this.alternateClassifier = StringUtils.isBlank(alternateClassifier) ? null : alternateClassifier.trim();
		this.finalName = finalName;
	}

	@Override
	public void exec() throws MojoExecutionException {

		packageWithAssembly(APPJAR_ASSEMBLY_NAME, //
				element("attach", "false"), //
				element("archive", //
						element("addMavenDescriptor", "true"), //
						element("manifest", //
								element("mainClass", AppJarBoot.class.getName()), //
								element("addDefaultImplementationEntries", "true"), //
								element("addDefaultSpecificationEntries", "true")), //
						element("manifestEntries", //
								element(MagicAppJarBoot.MF_APPJAR_MAIN_CLASS, mainClass))//
		));

		String classifier = APPJAR_ASSEMBLY_ID;
		File buildDir = new File(context.getProject().getBuild().getDirectory());
		File generated = new File(buildDir, finalName + "-" + APPJAR_ASSEMBLY_ID + "." + APPJAR_ASSEMBLY_EXT);
		if (alternateClassifier != null && !APPJAR_ASSEMBLY_ID.equals(alternateClassifier)) {
			// Enable custom classifiers

			classifier = alternateClassifier;

			File dstArchive = new File(buildDir, finalName + "-" + alternateClassifier + "." + APPJAR_ASSEMBLY_EXT);
			generated.renameTo(dstArchive);
			generated = dstArchive;
		}

		// Attach
		context.getProjectHelper().attachArtifact(context.getProject(), APPJAR_ASSEMBLY_EXT, classifier, generated);

	}

	protected void packageWithAssembly(String ref, Element... elements) throws MojoExecutionException {

		List<Element> config = new ArrayList<Element>();
		config.add(element("descriptorRefs", element("descriptorRef", ref)));
		if (elements != null) {
			config.addAll(Arrays.asList(elements));
		}

		PluginDescriptor currentPlugin = context.getPlugin();
		execMojo(MagicGoals.ASSEMBLY_SINGLE, //
				configuration(elementArray(config)), //
				singleDependency(currentPlugin.getGroupId(), currentPlugin.getArtifactId(), currentPlugin.getVersion()));

	}

}
