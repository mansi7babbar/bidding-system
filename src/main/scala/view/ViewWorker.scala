package view

import akka.actor.Status.Success
import akka.actor.{Actor, ActorRef, Props}
import view.listener.HTTPAPIListener
import view.publisher.HTTPAPIPublisher
import work.bid.BidWork

object ViewWorker {
  def props(controller: ActorRef): Props = Props(new ViewWorker(controller))
}

class ViewWorker(m_controller: ActorRef) extends Actor {
  val m_httpAPIListener = context.actorOf(HTTPAPIListener.props(m_controller), "http-api-listener")
  val m_httpAPIPublisher = context.actorOf(HTTPAPIPublisher.props(), "http-api-publisher")

  override def receive: Receive = {
    case bidWorkSuccess@Success(_: BidWork) => m_httpAPIPublisher ! bidWorkSuccess
  }
}
