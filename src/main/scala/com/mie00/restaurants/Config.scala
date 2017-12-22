package com.lightbend.akka.http.sample

import com.typesafe.config.ConfigFactory

object Config {
  val conf = ConfigFactory.load("application.json");
}
