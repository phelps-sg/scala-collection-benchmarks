
val ProfileQA  = config("ea") extend Runtime

scalaVersion := "2.13.8"

name := """scala-collection-benchmarks"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-Xdisable-assertions")

enablePlugins(JmhPlugin)

