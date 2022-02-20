package work.bid

import protocol.bid.{BidRequest, BidResponse}

case class BidWork(bidRequest: BidRequest, bidResponse: Option[BidResponse], completer: BidResponse => Unit)
