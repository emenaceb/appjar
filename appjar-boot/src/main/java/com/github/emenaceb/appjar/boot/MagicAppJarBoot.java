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
package com.github.emenaceb.appjar.boot;

/**
 * AppJar Boot constants.
 * 
 * @author emenaceb
 *
 */
public class MagicAppJarBoot {

	public static final String MF_APPJAR_MAIN_CLASS = "AppJar-Main-Class";

	public static final String SP_APPJAR_NO_BANNER = "appjar.noBanner";

	public static final String LIB_PREFIX = "lib/";

	public static final String MAIN_LIB = "appjar-main";

	public static final String MAIN_LIB_PATH = LIB_PREFIX + MAIN_LIB;

	public static final String MAVEN_APPJAR_BOOT_ARTIFACT_ID = "appjar-boot";

	public static final String MAVEN_APPJAR_GROUP_ID = "com.github.emenaceb.appjar";

	public static final String MAVEN_APPJAR_BOOT_INFO_PATH = "META-INF/maven/" + MAVEN_APPJAR_GROUP_ID + "/" + MAVEN_APPJAR_BOOT_ARTIFACT_ID + "/";

}
