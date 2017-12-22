package com.mie00.restaurants

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging

import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import com.mie00.restaurants.RestaurantRegistryActor._
import akka.pattern.ask
import akka.util.Timeout
import spray.json.DefaultJsonProtocol._
import scala.util.{ Failure, Success }

import reactivemongo.api.commands.WriteResult

//#restaurant-routes-class
trait RestaurantRoutes extends JsonSupport {
  //#restaurant-routes-class

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[RestaurantRoutes])

  // other dependencies that RestaurantRoutes use
  def restaurantRegistryActor: ActorRef

  // Required by the `ask` (?) method below
  val t = Config.conf.getDuration("timeout").toNanos
  implicit lazy val timeout = Timeout(scala.concurrent.duration.Duration.fromNanos(t): scala.concurrent.duration.FiniteDuration)

  //#all-routes
  //#restaurants-get-post
  //#restaurants-get-delete   
  lazy val restaurantRoutes: Route =
    pathPrefix("api") {
      pathPrefix("restaurants") {
        concat(
          //#restaurants-get-delete
          pathEnd {
            concat(
              get {
                parameters("closed".as[Boolean].?) { (closed) =>
                  {
                    val restaurantsFuture: Future[Future[List[Restaurant]]] =
                      (restaurantRegistryActor ? GetRestaurants(closed)).mapTo[Future[List[Restaurant]]]
                    complete(restaurantsFuture)
                  }
                }
              },
              post {
                entity(as[RestaurantData]) { restaurant =>
                  val restaurantCreated: Future[Future[WriteResult]] =
                    (restaurantRegistryActor ? CreateRestaurant(restaurant)).mapTo[Future[WriteResult]]
                  onSuccess(restaurantCreated) { result =>
                    log.info("Created restaurant {}", result.toString)
                    complete((StatusCodes.Created))
                  }
                }
              }
            )
          },
          //#restaurants-get-post
          //#restaurants-get-delete
          path(Segment) { uuid =>
            concat(
              put {
                entity(as[RestaurantData]) { restaurant =>
                  val restaurantUpdated: Future[Future[WriteResult]] =
                    (restaurantRegistryActor ? UpdateRestaurant(uuid, restaurant)).mapTo[Future[WriteResult]]
                  onComplete(restaurantUpdated) {
                    case Success(result) => {
                      log.info("Updated restaurant [{}]: {}", result.toString)
                      complete((StatusCodes.OK))
                    }
                    case Failure(ex) => complete((StatusCodes.NotFound, s"An error occurred: ${ex.getMessage}"))
                  }
                }
              }
            )
          }
        )
        //#restaurants-get-delete
      }
    }
  //#all-routes
}
