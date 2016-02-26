package com.github.emenaceb.appjar.maven.utils;

import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;

import com.github.emenaceb.appjar.maven.executor.ExecutorContext;
import com.github.emenaceb.appjar.maven.executor.GoalDescriptor;

public class PluginUtils {

	public static Plugin resolveProjectPlugin(ExecutorContext ctx, GoalDescriptor goal) {
		return ctx.getProject().getPlugin(goal.getGroupId() + ":" + goal.getArtifactId());
	}

	public static Plugin resolveNewPlugin(ExecutorContext ctx, GoalDescriptor goal, List<Dependency> dependencies) {
		String version = goal.getDefaultVersion();
		Plugin p = resolveProjectPlugin(ctx, goal);
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

}
