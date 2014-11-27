name := "exprezz"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.22.0"
)

initialize := {
   val javaVersion = sys.props("java.version")
   val isJava8 = javaVersion.startsWith("1.8")
   require(isJava8, "Java 8 is required for this project")
}
