package view.publisher

import akka.actor.Status.Success
import akka.actor.{Actor, Props}
import protocol.bid.BidResponse
import work.bid.BidWork

object HTTPAPIPublisher {
  def props(): Props = Props(new HTTPAPIPublisher)
}

class HTTPAPIPublisher extends Actor {
  override def receive: Receive = {
    case _@Success(BidWork(_, bidResponse, completer)) => handleBidWorkSuccess(bidResponse, completer)
  }

  def handleBidWorkSuccess(bidResponseOption: Option[BidResponse], completer: BidResponse => Unit) {
    bidResponseOption.foreach {
      bidResponse => completer(bidResponse)
    }
  }
}
