package model

import akka.actor.Status.Success
import akka.actor.{Actor, ActorRef, Props}
import model.repository.CampaignRepositoryImpl
import model.service.CampaignService
import work.bid.BidWork

object ModelWorker {
  def props(controller: ActorRef): Props = Props(new ModelWorker(controller))
}

class ModelWorker(m_controller: ActorRef) extends Actor {
  val m_campaignRepository = new CampaignRepositoryImpl
  val m_campaignService = context.actorOf(CampaignService.props(self, m_campaignRepository), "campaign-service")

  override def receive: Receive = {
    case bidWork@BidWork(_, None, _) => m_campaignService ! bidWork
    case bidWork@BidWork(_, Some(_), _) => m_controller ! Success(bidWork)
  }
}
