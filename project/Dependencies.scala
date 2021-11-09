import sbt._


object Dependencies extends AutoPlugin {

  object autoImport {
    /**
     * ------------------------------
     * Compile/hard dependencies
     * ------------------------------
     */

    /**
     * ------------------------------
     * Test dependencies
     * ------------------------------
     */
    //val junit = "junit" % "junit" % "4.11"
    val junit = "junit" % "junit" % "4.13.2"
    val `junit-interface` = "com.github.sbt" % "junit-interface" % "0.13.2"
  }

}
