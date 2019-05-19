name := "ScalableComputing"


version := "1.0-SNAPSHOT"

lazy val `scalablecomputing` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"


libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test
libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.binders._"
