name := "mastermind"

version := "0.1"

scalaVersion := "2.13.1"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
scalacOptions ++= Seq(
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings"
)

libraryDependencies += "org.typelevel"        %% "cats-core"    % "2.1.1"
libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1"
