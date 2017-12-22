package com.mie00.restaurants.migration

import spray.json._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.concurrent._
import scala.util.{ Success, Failure }

import com.mie00.restaurants.Database
import com.mie00.restaurants.Restaurant
import com.mie00.restaurants.JsonSupport

import ExecutionContext.Implicits.global

object Migration extends App with JsonSupport {
  val source = scala.io.Source.fromFile("assets/sample-restaurant-data.json").mkString
  val json = source.parseJson
  val restaurants = json.convertTo[List[Restaurant]]
  Database.collection
  Thread.sleep(5000)
  val seq = Future.sequence(restaurants.map(Database.create(_)))
  seq.onComplete {
    case Success(i) => println("Migration Completed")
    case Failure(e) => e.printStackTrace()
  }
  seq.map(_ => {})
  // TODO: Exit after migration
}
