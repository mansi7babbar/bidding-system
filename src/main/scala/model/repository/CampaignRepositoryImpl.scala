package model.repository

import protocol.bid.{Device, Site, User}
import protocol.campaign.{Banner, Campaign, Targeting}

class CampaignRepositoryImpl extends CampaignRepository {
  val activeCampaigns = Seq(
    Campaign(
      id = 1,
      country = "LT",
      targeting = Targeting(
        targetedSites = Seq(Site("0006a522ce0f4bbbbaa6b3c38cafaa0f", "fake.tld"))
      ),
      banners = List(
        Banner(
          id = 1,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 5d
    )
  )

  override def getCampaignsBySiteAndCountry(
                                             siteId: String,
                                             user: Option[User],
                                             device: Option[Device]
                                           ): Seq[Campaign] = {
    activeCampaigns
      .filter(_.targeting.targetedSites.map(_.id).contains(siteId))
      .filter {
        campaign => {
          val isDeviceCountryMatching = device.flatMap(_.geo.flatMap(_.country.map(_ == campaign.country))).getOrElse(false)
          if (!isDeviceCountryMatching) {
            user.flatMap(_.geo.flatMap(_.country.map(_ == campaign.country))).getOrElse(false)
          }
          else isDeviceCountryMatching
        }
      }
  }
}
