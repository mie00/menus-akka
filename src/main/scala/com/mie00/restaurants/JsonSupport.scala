package com.mie00.restaurants

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.mie00.restaurants.RestaurantRegistryActor.ActionPerformed
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val restaurantDataJsonFormat = jsonFormat18(RestaurantData)
  implicit val restaurantJsonFormat = jsonFormat2(Restaurant)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
