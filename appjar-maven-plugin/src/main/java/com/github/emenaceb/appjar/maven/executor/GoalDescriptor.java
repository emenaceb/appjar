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

/**
 * Plugin goal execution descriptor.
 * 
 * @author emenaceb
 *
 */
public class GoalDescriptor {

	private String groupId;

	private String artifactId;

	private String defaultVersion;

	private String goal;

	public GoalDescriptor(String groupId, String artifactId, String defaultVersion, String goal) {
		super();
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.defaultVersion = defaultVersion;
		this.goal = goal;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoalDescriptor other = (GoalDescriptor) obj;
		if (artifactId == null) {
			if (other.artifactId != null)
				return false;
		} else if (!artifactId.equals(other.artifactId))
			return false;
		if (defaultVersion == null) {
			if (other.defaultVersion != null)
				return false;
		} else if (!defaultVersion.equals(other.defaultVersion))
			return false;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		return true;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getDefaultVersion() {
		return defaultVersion;
	}

	public String getGoal() {
		return goal;
	}

	public String getGroupId() {
		return groupId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result + ((defaultVersion == null) ? 0 : defaultVersion.hashCode());
		result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GoalDescriptor [");
		builder.append(groupId);
		builder.append(":");
		builder.append(artifactId);
		builder.append(":");
		builder.append(defaultVersion);
		builder.append(":");
		builder.append(goal);
		builder.append("]");
		return builder.toString();
	}

}
