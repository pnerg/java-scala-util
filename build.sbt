import sbt.Keys.{javacOptions, scalaVersion}

organization  := "org.dmonix.functional"
version := "1.11.2"
name := "java-scala-utils"

libraryDependencies ++= Seq(
      junit % Test,
      `junit-interface` % Test
)

crossPaths := false
autoScalaLibrary := false

javacOptions in (Compile, doc) := Seq("-source", "1.8")
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

jacocoReportSettings := JacocoReportSettings()
      .withThresholds(
        JacocoThresholds(
          instruction = 100,
          method = 100,
          branch = 95,
          complexity = 95,
          line = 100,
          clazz = 100)
      )
      .withFormats(JacocoReportFormats.HTML, JacocoReportFormats.XML)