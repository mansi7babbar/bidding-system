package view.listener

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import protocol.bid.BidProtocol.{BidRequestFormat, BidResponseFormat}
import protocol.bid.{BidRequest, BidResponse}
import protocol.campaign.CampaignProtocol._
import work.bid.BidWork

import scala.concurrent.ExecutionContext

object HTTPAPIListener {
  def props(controller: ActorRef): Props = Props(new HTTPAPIListener(controller))
}

class HTTPAPIListener(m_controller: ActorRef) extends Actor {
  implicit val m_system: ActorSystem = context.system
  implicit val m_dispatcher: ExecutionContext = m_system.dispatcher

  final val BID_REQUEST = "BidRequest"

  val bidRequest: Route = {
    path(BID_REQUEST) {
      cors(settings = CorsSettings.defaultSettings) {
        post {
          entity(as[BidRequest]) {
            bidRequest => {
              completeWith(instanceOf[BidResponse]) {
                completer => {
                  m_controller ! BidWork(bidRequest, None, completer)
                }
              }
            }
          }
        }
      }
    }
  }

  val m_serviceHost: String = "localhost"
  val m_servicePort: Int = 8080
  private val m_serviceBindingRoute: Route = {
    Route.seal {
      bidRequest
    }
  }
  var m_serverBinding: Option[ServerBinding] = None

  override def preStart() {
    Http().newServerAt(m_serviceHost, m_servicePort).bind(m_serviceBindingRoute)
  }

  override def postStop() {
    m_serverBinding.foreach(_.unbind())
  }

  override def receive: Receive = {
    case serverBinding: ServerBinding => handleServerBinding(serverBinding)
  }

  private def handleServerBinding(serverBinding: ServerBinding) {
    m_serverBinding = Some(serverBinding)
  }
}
