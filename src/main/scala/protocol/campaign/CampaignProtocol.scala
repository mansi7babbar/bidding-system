package protocol.campaign

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import protocol.bid.Site
import spray.json.DefaultJsonProtocol._

case class Campaign(id: Int, country: String, targeting: Targeting, banners: List[Banner], bid: Double)

case class Targeting(targetedSites: Seq[Site])

case class Banner(id: Int, src: String, width: Int, height: Int)

object CampaignProtocol extends SprayJsonSupport {
  implicit val SiteFormat = jsonFormat2(Site)
  implicit val TargetingFormat = jsonFormat1(Targeting)
  implicit val BannerFormat = jsonFormat4(Banner)
  implicit val CampaignFormat = jsonFormat5(Campaign)
}
