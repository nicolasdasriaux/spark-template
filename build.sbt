val root = (project in file("."))
  .enablePlugins(ParadoxPlugin)
  .settings(
    organization := "fr.nikorada",
    name := "spark-template",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := "2.11.11",

    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.1.0" % Provided,
      "org.apache.spark" %% "spark-hive" % "2.1.0" % Provided,
      "com.holdenkarau" %% "spark-testing-base" % "2.1.0_0.6.0" % Test,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    ),

    javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),

    fork in Test := true,
    parallelExecution in Test := false,

    paradoxTheme := Some(builtinParadoxTheme("generic")),

    test in assembly := {},
    mainClass in assembly := Some("SparkSqlApp"),
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),

    assemblyExcludedJars in assembly := {
      val cp = (fullClasspath in assembly).value
      cp.filter(_.data.getName.contains("paradox-theme-generic"))
    },

    run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated,
    runMain in Compile := Defaults.runMainTask(fullClasspath in Compile, runner in (Compile, run)).evaluated
  )
