name := "bidding-system"

version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.6.18",
  "com.typesafe.akka" %% "akka-actor" % "2.6.18",
  "com.typesafe.akka" %% "akka-http" % "10.2.8",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.8",
  "ch.megard" %% "akka-http-cors" % "1.1.3",
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)
