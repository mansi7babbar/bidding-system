package mockup

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import protocol.bid.BidProtocol._
import protocol.bid.{BidRequest, BidResponse}
import protocol.campaign.Banner

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.{DurationInt, FiniteDuration}

object HTTPServerMockup {
  implicit val system = ActorSystem("BiddingSystemTest")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val m_serviceHost: String = "localhost"
  val m_servicePort: Int = 8081
  val systemDuration: FiniteDuration = 5.seconds
  val m_http = Http()

  final val BID_REQUEST = "BidRequest"

  val expectedBidResponse = BidResponse(status = true, "", "SGu1Jpq1IO", 3.12123, Some("1"),
    Some(Banner(1, "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg", 300, 250)))

  val bidRequest: Route = {
    path(BID_REQUEST) {
      cors(settings = CorsSettings.defaultSettings) {
        post {
          entity(as[BidRequest]) {
            _ => {
              complete(expectedBidResponse)
            }
          }
        }
      }
    }
  }

  val m_serviceBindingRoute: Route = {
    Route.seal {
      bidRequest
    }
  }

  def startServer() {
    Http().bindAndHandle(m_serviceBindingRoute, m_serviceHost, m_servicePort)
  }

  def stopServer() {
    system.terminate()
  }
}
