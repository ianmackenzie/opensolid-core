// Set up project
lazy val root = project.in(file(".")).
  settings(
    name := "opensolid-core",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings"),
    licenses += ("MPL-2.0", url("https://www.mozilla.org/en-US/MPL/2.0/")),
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.5" % "test",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    logBuffered in Test := true,
    bintrayVcsUrl := Some("git@github.com:ianmackenzie/opensolid-core.git")
  )

// Set up publishing of generated ScalaDoc to GitHub Pages
site.settings
ghpages.settings
site.includeScaladoc()
git.remoteRepo := "https://github.com/ianmackenzie/opensolid-core.git"
