// @formatter:off

name         := "opencv2-cookbook"
organization := "javacv.examples"

val javacppVersion = "1.3"
version       := javacppVersion
scalaVersion := "2.12.1"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xlint")

//// Some dependencies like `javacpp` are packaged with maven-plugin packaging
//classpathTypes += "maven-plugin"

// Platform classifier for native library dependencies
val platform = org.bytedeco.javacpp.Loader.getPlatform
// Libraries with native dependencies
val bytedecoPresetLibs = Seq("opencv" -> s"3.1.0-$javacppVersion").flatMap {
  case (lib, ver) => Seq(
    // Add both: dependency and its native binaries for the current `platform`
    "org.bytedeco.javacpp-presets" % lib % ver withSources() withJavadoc(),
    "org.bytedeco.javacpp-presets" % lib % ver classifier platform
  )
}

libraryDependencies ++= Seq(
  "org.bytedeco" % "javacpp" % javacppVersion withSources() withJavadoc(),
  "org.bytedeco" % "javacv" % javacppVersion withSources() withJavadoc(),
  "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2",
  "junit"                   % "junit"           % "4.12" % "test",
  "com.novocode"            % "junit-interface" % "0.11" % "test"
) ++ bytedecoPresetLibs

// Used for testing local builds and snapshots of JavaCPP/JavaCV
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  // Use local maven repo for local javacv builds
  "Local Maven Repository" at "file:///" + Path.userHome.absolutePath + "/.m2/repository"
)

autoCompilerPlugins := true

// fork a new JVM for 'run' and 'test:run'
fork := true
// add a JVM option to use when forking a JVM for 'run'
javaOptions += "-Xmx1G"

// Set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => "sbt:" + Project.extract(state).currentRef.project + "> " }