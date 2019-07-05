name := "cats-test"

version := "0.1"

scalaVersion := "2.11.12"

scalacOptions ++= Seq("-Ypartial-unification", "-language:higherKinds", "-language:implicitConversions")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.0.0-M4",
  "org.typelevel" %% "cats-effect" % "2.0.0-M4",
  "io.monix" %% "monix" % "3.0.0-RC3",
  "com.carrotsearch" % "hppc" % "0.8.1",
  "com.typesafe.akka" %% "akka-actor" % "2.5.23",
  "com.github.mpilquist" %% "simulacrum" % "0.19.0"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
