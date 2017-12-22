package com.mie00.restaurants

import com.typesafe.config.ConfigFactory

object Config {
  val conf = ConfigFactory.load("application.json");
}
