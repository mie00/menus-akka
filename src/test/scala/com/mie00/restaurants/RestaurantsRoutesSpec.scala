package com.mie00.restaurants

//#test-top
import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }

//#set-up
class RestaurantRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest
    with RestaurantRoutes {
  //#test-top

  // Here we need to implement all the abstract members of RestaurantRoutes.
  // We use the real RestaurantRegistryActor to test it while we hit the Routes, 
  // but we could "mock" it by implementing it in-place or by using a TestProbe() 
  override val restaurantRegistryActor: ActorRef =
    system.actorOf(RestaurantRegistryActor.props, "restaurantRegistry")

  lazy val routes = restaurantRoutes

  //#set-up

  //#actual-test
  "RestaurantRoutes" should {
    "return no restaurants if no present (GET /api/restaurants)" in {
      // note that there's no need for the host part in the uri:
      val request = HttpRequest(uri = "/api/restaurants")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
      }
    }
    //#actual-test

    //#testing-post
    "be able to add restaurants (POST /api/restaurants)" in {
      val restaurantEntity = HttpEntity(ContentTypes.`application/json`, """{
        "enName": "3al Ahwa Cafe",
        "arName": "عالقهوة كافيه",
        "state": "PUBLISHED",
        "routingMethod": null,
        "logo": "i3qf6gym1p833di.jpg",
        "coverPhoto": null,
        "enDescription": "",
        "arDescription": null,
        "shortNumber": "",
        "facebookLink": "",
        "twitterLink": "",
        "youtubeLink": "",
        "website": null,
        "onlinePayment": false,
        "client": false,
        "pendingInfo": true,
        "pendingMenu": true,
        "closed": false
      }""")

      // using the RequestBuilding DSL:
      val request = Post("/api/restaurants").withEntity(restaurantEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.Created)
      }
    }
    //#testing-post
    //#testing-put
    "be able to add restaurants (PUT /api/restaurants/11111111-1111-1111-1111-11111111111)" in {
      val restaurantEntity = HttpEntity(ContentTypes.`application/json`, """{
        "enName": "3al Ahwa Cafe",
        "arName": "عالقهوة كافيه",
        "state": "PUBLISHED",
        "routingMethod": null,
        "logo": "i3qf6gym1p833di.jpg",
        "coverPhoto": null,
        "enDescription": "",
        "arDescription": null,
        "shortNumber": "",
        "facebookLink": "",
        "twitterLink": "",
        "youtubeLink": "",
        "website": null,
        "onlinePayment": false,
        "client": false,
        "pendingInfo": true,
        "pendingMenu": true,
        "closed": false
      }""")

      // using the RequestBuilding DSL:
      val request = Put("/api/restaurants/11111111-1111-1111-1111-11111111111").withEntity(restaurantEntity)

      request ~> routes ~> check {
        cancel
        // TODO: fix
        status should ===(StatusCodes.NotFound)
      }
    }
    //#testing-put

    //#actual-test
  }
  //#actual-test

  //#set-up
}
//#set-up
