import sbt.Keys._
import sbt.{AutoPlugin, Developer, PluginTrigger, Plugins, ScmInfo, plugins, _}
import xerial.sbt.Sonatype._
import xerial.sbt._

/**
 * Project settings related to sbt-sonatype.
 * Required to be able to publish to Maven central
 *
 * @author Peter Nerg
 */
object SonatypeSettings extends AutoPlugin {
  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = plugins.JvmPlugin && Sonatype

  object autoImport extends SonatypeKeys

  import autoImport._

  private val projectName = "java-scala-utils"

  override lazy val projectSettings = Seq(
    startYear := Some(2015),
    publishTo := sonatypePublishTo.value,
    publishMavenStyle := true,
    licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    sonatypeProjectHosting := Some(GitHubHosting("pnerg", projectName, "")),
    homepage := Some(url(s"https://github.com/pnerg/$projectName")),
    scmInfo := Some(
      ScmInfo(
        url(s"https://github.com/pnerg/$projectName"),
        s"scm:git@github.com:pnerg/$projectName.git"
      )
    ),
    developers := List(
      Developer(id = "pnerg", name = "Peter Nerg", email = "", url = url("https://github.com/pnerg"))
    )
  )
}