/*
  To run tests with a specific mysql driver use this java option:
    -Dmysql.driver=mysql5
 */

val mysqlDriver = sys.props.getOrElse("mysql.driver", "mariadb") match {
  case "mariadb" => "org.mariadb.jdbc" % "mariadb-java-client"  % "2.+"
  case "mysql5"  => "mysql"            % "mysql-connector-java" % "5.+"
  case "mysql8"  => "mysql"            % "mysql-connector-java" % "8.+"
  case _ =>
    throw new IllegalArgumentException(
      "java option mysql.driver should be one of { mariadb, mysql5, mysql8 }"
    )
}

lazy val root = project
  .withId("memsql-spark-connector")
  .in(file("."))
  .settings(
    name := "memsql-spark-connector",
    organization := "com.memsql",
    version := "3.0.0-spark-2.3-beta",
    scalaVersion := "2.11.11",
    licenses += "Apache-2.0" -> url(
      "http://opensource.org/licenses/Apache-2.0"
    ),
    resolvers += "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven",
    libraryDependencies ++= Seq(
      // runtime dependencies
      "org.apache.spark"       %% "spark-core"             % "2.3.4",
      "org.apache.spark"       %% "spark-sql"              % "2.3.4",
      "org.apache.commons"     % "commons-dbcp2"           % "2.7.0",
      "org.scala-lang.modules" % "scala-java8-compat_2.11" % "0.9.0",
      // test dependencies
      "org.scalatest"  %% "scalatest" % "3.1.0" % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.1" % Test,
      "mrpowers"       % "spark-daria" % "0.35.0-s_2.11" % Test,
      "MrPowers"       % "spark-fast-tests" % "0.20.0-s_2.11" % Test,
      mysqlDriver      % Test
    ),

    Test / testOptions += Tests.Argument("-oF"),
    // Test / testOptions += Tests.Argument("-oN"),
    Test / fork := true,
    Test / javaOptions ++= Seq("-Xms512M", "-Xmx2048M"),
  )
