# App Jar

AppJar packages Java application in a single executable jar just like Maven Shade Plugin or OneJar Plugin.


## Similar Software 
 
[Maven Shade Plugin](https://maven.apache.org/plugins/maven-shade-plugin/) combines severla jars in a single jar combining them.  
It's unable to handle properly artifacts that packages more than one jar that contain the same file.  
It tries to solve problems with some files like service files combining them, but sometimes this is not enough, 
for instance, when jars depends on its Manifest information to work.

[OneJar Maven Plugin](https://github.com/jolira/onejar-maven-plugin) (Maven adaptation for [OneJar](http://one-jar.sourceforge.net/)) 
packages jars inside other jar without unpacking them.  
It uses a classloader to load classes from the inner jars. The way it does that forces to use JarInputStreams and iterate over entries 
each time it has to read a class, affecting performance.  
In the other hand, it uses jar: an file: URL protocols to solve class loading mechanism and it leads to some problems with some software that uses those
protocols (I've found some problems with XML default entity resolvers that handles those protocols).

## AppJar basics

AppJar unpacks all dependencies just like Maven Shade Plugin, but instead of unpacking them into the same folder it unpacks the jars in separate paths
inside the jar.

It allows having more than one Manifest inside the main jar.

It register a new protocol to handle the class loading so it doesn't affect software that handle standard protocols directly.  
 
# Usage (Maven)

To use AppJar you must configure the plugin in the `pom.xml` file. 

## Add plugin

```xml
	<plugin>
		<groupId>com.github.emenaceb.appjar</groupId>
		<artifactId>appjar-maven-plugin</artifactId>
		<version>0.9.1</version>
		<executions>
			<execution>
				<id>generate_appjar</id>
				<phase>package</phase>
				<goals>
					<goal>simple</goal>
				</goals>
				<inherited>false</inherited>
			</execution>
		</executions>
	</plugin>
```
That creates and attach to the Maven build an artifact with `app` classifier that contains all artifact's dependencies.

## Configure main class

It reads from the `maven-jar-plugin` the Main-Class configuration:

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-jar-plugin</artifactId>
	<configuration>
		<archive>
			<manifest>
				<mainClass>io.vertx.core.Launcher</mainClass>
				<addDefaultImplementationEntries>true</addDefaultImplementationEntries>
				<addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
			</manifest>
			<manifestEntries>
				<Main-Verticle>my.project.MainVerticle</Main-Verticle>
			</manifestEntries>
		</archive>
	</configuration>
</plugin>
```
 
You also can override this class adding `mainClass` property to the plugin configuration.

```xml
	<plugin>
		<groupId>com.github.emenaceb.appjar</groupId>
		<artifactId>appjar-maven-plugin</artifactId>
		...
		
		<configuration>
			<mainClass>my.project.Main</mainClass>
		</configuration>
		...
	</plugin>
```
 
## Change classifier
 
You can change the default `app` classifier to other:
 
 ```xml
	<plugin>
		<groupId>com.github.emenaceb.appjar</groupId>
		<artifactId>appjar-maven-plugin</artifactId>
		...
		
		<configuration>
			<alternateClassifier>fat</alternateClassifier>
		</configuration>
		...
	</plugin>
```

## Include custom banner file

In addition to AppJar banner you can add a custom banner to be shown during startup in the starndar output.


 ```xml
	<plugin>
		<groupId>com.github.emenaceb.appjar</groupId>
		<artifactId>appjar-maven-plugin</artifactId>
		...
		
		<configuration>
			<bannerFile>src/main/appjar/banner.txt</bannerFile>
		</configuration>
		...
	</plugin>
```

This banner may contain any Maven Project property like `${project.version}`.

### Disable default banner

If you want to disable the big default AppJar banner, you can do it adding the `-Dappjar.noBanner` to the command line and the
default banner will be replaced with a more discreet message:

```
 AppJar v 0.9.1  (c) emenaceb 2016
----------------------------------------------------------------------
```

If you've defined a custom banner the message looks like :

```
 Powered by AppJar v 0.9.1  (c) emenaceb 2016
----------------------------------------------------------------------
```

Feel free to disable the default AppJar banner, but keep in mind that every time you do that a little kitty dies. 
 
 
