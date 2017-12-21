package com.lightbend.akka.http.sample

import akka.actor.{ Actor, ActorLogging, Props }
import io.jvm.uuid._

//#restaurant-case-classes
case class RestaurantData(enName: String, arName: String, state: String, routingMethod: Option[String], logo: Option[String], coverPhoto: Option[String],
  enDescription: Option[String], arDescription: Option[String], shortNumber: Option[String],
  facebookLink: Option[String], twitterLink: Option[String], youtubeLink: Option[String], website: Option[String],
  onlinePayment: Boolean, client: Boolean, pendingInfo: Boolean, pendingMenu: Boolean, closed: Boolean)

case class Restaurant(uuid: String, data: RestaurantData)
//#restaurant-case-classes

object RestaurantRegistryActor {
  final case class ActionPerformed(description: String)
  final case class GetRestaurants(closed: Option[Boolean])
  final case class CreateRestaurant(restaurant: RestaurantData)
  final case class UpdateRestaurant(uuid: String, restaurant: RestaurantData)

  def props: Props = Props[RestaurantRegistryActor]
}

class RestaurantRegistryActor extends Actor with ActorLogging {
  import RestaurantRegistryActor._

  var restaurants = collection.mutable.Map[String, RestaurantData]()

  def receive: Receive = {
    case GetRestaurants(closed) =>
      sender() ! ((restaurants.map { case (k, v) => Restaurant(k, v) }(collection.breakOut): List[Restaurant]).filter { case x => closed == None || x.data.closed == closed.get })
    case CreateRestaurant(restaurant) =>
      val uuid = UUID.random.string
      restaurants += { uuid -> restaurant }
      sender() ! ActionPerformed(s"Restaurant ${uuid} created.")
    case UpdateRestaurant(uuid, restaurant) =>
      if ((restaurants get uuid) != None) {
        restaurants(uuid) = restaurant
        sender() ! ActionPerformed(s"Restaurant ${uuid} updated.")
      } else {
        sender() ! ActionPerformed(s"Restaurant ${uuid} not found.")
      }
  }
}
