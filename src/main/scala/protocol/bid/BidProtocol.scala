package protocol.bid

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import protocol.campaign.Banner
import protocol.campaign.CampaignProtocol.BannerFormat
import spray.json.DefaultJsonProtocol._

case class BidRequest(
                       id: String,
                       imp: Option[List[Impression]],
                       site: Site,
                       user: Option[User],
                       device: Option[Device]
                     )

case class Impression(
                       id: String,
                       wmin: Option[Int],
                       wmax: Option[Int],
                       w: Option[Int],
                       hmin: Option[Int],
                       hmax: Option[Int],
                       h: Option[Int],
                       bidFloor: Option[Double]
                     )

case class Site(id: String, domain: String)

case class User(id: String, geo: Option[Geo])

case class Device(id: String, geo: Option[Geo])

case class Geo(country: Option[String])

case class BidResponse(
                        status: Boolean,
                        id: String,
                        bidRequestId: String,
                        price: Double,
                        adid: Option[String],
                        banner: Option[Banner]
                      )

object BidProtocol extends SprayJsonSupport {
  implicit val ImpressionFormat = jsonFormat8(Impression)
  implicit val SiteFormat = jsonFormat2(Site)
  implicit val GeoFormat = jsonFormat1(Geo)
  implicit val UserFormat = jsonFormat2(User)
  implicit val DeviceFormat = jsonFormat2(Device)
  implicit val BidRequestFormat = jsonFormat5(BidRequest)
  implicit val BidResponseFormat = jsonFormat6(BidResponse)
}
