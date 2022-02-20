package model.service

import akka.actor.{Actor, ActorRef, Props}
import model.repository.CampaignRepository
import protocol.bid._
import protocol.campaign.Banner
import work.bid.BidWork

object CampaignService {
  def props(modelWorker: ActorRef, campaignRepository: CampaignRepository): Props = Props(new CampaignService(modelWorker, campaignRepository))
}

class CampaignService(m_modelWorker: ActorRef, m_campaignRepository: CampaignRepository) extends Actor {
  override def receive: Receive = {
    case work@BidWork(BidRequest(bidRequestId, impression, Site(siteId, _), user, device), _, _) => bidWork(work, bidRequestId, impression, siteId, user, device)
  }

  def bidWork(bidWork: BidWork,
              bidRequestId: String,
              impression: Option[List[Impression]],
              siteId: String,
              user: Option[User],
              device: Option[Device]) {
    val defaultBidResponse = BidResponse(status = true, "", bidRequestId, 0.0, None, None)
    val bidResponse = getMatchingCampaign(impression, siteId, user, device, defaultBidResponse)

    m_modelWorker ! bidWork.copy(bidResponse = Some(bidResponse))
  }

  def getMatchingCampaign(
                           impressions: Option[List[Impression]],
                           siteId: String,
                           user: Option[User],
                           device: Option[Device],
                           bidResponse: BidResponse
                         ): BidResponse = {
    val campaignsWithMatchingSiteAndCountry = m_campaignRepository.getCampaignsBySiteAndCountry(siteId, user, device)
    impressions.map {
      _.map {
        impression => {
          campaignsWithMatchingSiteAndCountry.map {
            campaign => {
              campaign.banners.map {
                banner =>
                  if (isMatchingBannerSize(impression, banner)) {
                    return bidResponse.copy(
                      price = impression.bidFloor.getOrElse(bidResponse.price),
                      adid = Some(campaign.id.toString),
                      banner = Some(banner)
                    )
                  }
              }
            }
          }
        }
      }
    }
    bidResponse
  }

  def isMatchingBannerSize(impression: Impression, banner: Banner): Boolean = {
    val width = impression.w.getOrElse(0)
    val minWidth = impression.wmin.getOrElse(width)
    val maxWidth = impression.wmax.getOrElse {
      if (width == 0) Integer.MAX_VALUE else width
    }
    val height = impression.h.getOrElse(0)
    val minHeight = impression.hmin.getOrElse(height)
    val maxHeight = impression.hmax.getOrElse {
      if (height == 0) Integer.MAX_VALUE else height
    }
    val finalMinWidth = Math.min(width, minWidth)
    val finalMaxWidth = Math.max(width, maxWidth)
    val finalMinHeight = Math.min(height, minHeight)
    val finalMaxHeight = Math.max(height, maxHeight)

    banner.width >= finalMinWidth && banner.width <= finalMaxWidth && banner.height >= finalMinHeight && banner.height <= finalMaxHeight
  }
}
