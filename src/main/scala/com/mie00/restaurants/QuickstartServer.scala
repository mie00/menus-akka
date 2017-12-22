package com.lightbend.akka.http.sample

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.{ ExecutionContext, Future }
import scala.io.StdIn

//#main-class
object QuickstartServer extends App with RestaurantRoutes {

  // set up ActorSystem and other dependencies here
  //#main-class
  //#server-bootstrapping
  implicit val system: ActorSystem = ActorSystem("helloAkkaHttpServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  //#server-bootstrapping

  // Needed for the Future and its methods flatMap/onComplete in the end
  implicit val executionContext: ExecutionContext = system.dispatcher

  val restaurantRegistryActor: ActorRef = system.actorOf(RestaurantRegistryActor.props, "restaurantRegistryActor")

  //#main-class
  // from the RestaurantRoutes trait
  lazy val routes: Route = restaurantRoutes
  //#main-class

  //#http-server
  val host = Config.conf.getString("host")
  val port = Config.conf.getInt("port")
  val serverBindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, host, port)

  println(s"Server online at http://${host}:${port}/\nPress RETURN to stop...")

  StdIn.readLine()

  serverBindingFuture
    .flatMap(_.unbind())
    .onComplete { done =>
      done.failed.map { ex => log.error(ex, "Failed unbinding") }
      system.terminate()
    }
  //#http-server
  //#main-class
}
//#main-class
//
