package model.repository

import protocol.bid.{Device, User}
import protocol.campaign.Campaign

trait CampaignRepository {
  def getCampaignsBySiteAndCountry(siteId: String, user: Option[User], device: Option[Device]): Seq[Campaign]
}
