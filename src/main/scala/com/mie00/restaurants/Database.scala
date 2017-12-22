package com.mie00.restaurants

import reactivemongo.api._
import reactivemongo.bson.{ BSONDocument, BSONDocumentWriter, BSONDocumentReader, Macros }
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Database {
  val collection = connect()
  def connect(): BSONCollection = {

    val driver = new MongoDriver
    val mongourl = Config.conf.getString("mongodb.url")
    val connection = driver.connection(List(mongourl))

    val mongodbname = Config.conf.getString("mongodb.dbname")
    val db = connection(mongodbname)
    db.collection("restaurants")
  }

  implicit object RestaurantDataWtiter extends BSONDocumentWriter[RestaurantData] {
    def write(r: RestaurantData): BSONDocument =
      BSONDocument(
        "enName" -> r.enName, "arName" -> r.arName, "state" -> r.state, "routingMethod" -> r.routingMethod, "logo" -> r.logo, "coverPhoto" -> r.coverPhoto,
        "enDescription" -> r.enDescription, "arDescription" -> r.arDescription, "shortNumber" -> r.shortNumber,
        "facebookLink" -> r.facebookLink, "twitterLink" -> r.twitterLink, "youtubeLink" -> r.youtubeLink, "website" -> r.website,
        "onlinePayment" -> r.onlinePayment, "client" -> r.client, "pendingInfo" -> r.pendingInfo, "pendingMenu" -> r.pendingMenu, "closed" -> r.closed
      )
  }
  implicit object RestaurantDataReader extends BSONDocumentReader[RestaurantData] {
    def read(doc: BSONDocument): RestaurantData = {
      val routingMethod = doc.getAs[String]("routingMethod")
      val logo = doc.getAs[String]("logo")
      val coverPhoto = doc.getAs[String]("coverPhoto")
      val enDescription = doc.getAs[String]("enDescription")
      val arDescription = doc.getAs[String]("arDescription")
      val shortNumber = doc.getAs[String]("shortNumber")
      val facebookLink = doc.getAs[String]("facebookLink")
      val twitterLink = doc.getAs[String]("twitterLink")
      val youtubeLink = doc.getAs[String]("youtubeLink")
      val website = doc.getAs[String]("website")
      (for {
        enName <- doc.getAsTry[String]("enName")
        arName <- doc.getAsTry[String]("arName")
        state <- doc.getAsTry[String]("state")
        onlinePayment <- doc.getAsTry[Boolean]("onlinePayment")
        client <- doc.getAsTry[Boolean]("client")
        pendingInfo <- doc.getAsTry[Boolean]("pendingInfo")
        pendingMenu <- doc.getAsTry[Boolean]("pendingMenu")
        closed <- doc.getAsTry[Boolean]("closed")
      } yield RestaurantData(enName, arName, state, routingMethod, logo, coverPhoto,
        enDescription, arDescription, shortNumber,
        facebookLink, twitterLink, youtubeLink, website,
        onlinePayment, client, pendingInfo, pendingMenu, closed)).get
    }
  }

  implicit object RestaurantWtiter extends BSONDocumentWriter[Restaurant] {
    def write(r: Restaurant): BSONDocument =
      BSONDocument("uuid" -> r.uuid, "data" -> r.data)
  }

  implicit object RestaurantReader extends BSONDocumentReader[Restaurant] {
    def read(doc: BSONDocument): Restaurant = {
      (for {
        uuid <- doc.getAsTry[String]("uuid")
        data <- doc.getAsTry[RestaurantData]("data")
      } yield Restaurant(uuid, data)).get
    }
  }

  def findAllRestaurants(closed: Option[Boolean]): Future[List[Restaurant]] = {
    val query = BSONDocument("data.closed" -> closed)

    Database.collection
      .find(query)
      .cursor[Restaurant]
      .collect[List]()
  }
  def create(r: Restaurant): Future[WriteResult] = {
    Database.collection
      .insert(r)
  }
  def update(r: Restaurant): Future[WriteResult] = {
    val selector = BSONDocument("uuid" -> r.uuid)
    Database.collection
      .update(selector, r)
  }
}
