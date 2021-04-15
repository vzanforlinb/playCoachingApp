name := "couchingApp"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "com.google.inject" % "guice" % "5.0.1"
)

play.Project.playJavaSettings
