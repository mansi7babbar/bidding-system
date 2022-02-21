package bid

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import mockup.HTTPServerMockup._
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import protocol.bid.BidProtocol._
import protocol.bid._
import util.TestUtils._

import scala.concurrent.{Await, Future}

class BidAPITest extends FlatSpec with Matchers with BeforeAndAfterAll {
  val bidRequest = BidRequest("SGu1Jpq1IO",
    Some(List(Impression("1", Some(50), Some(300), Some(300), Some(100), Some(300), Some(250), Some(3.12123)))),
    Site("0006a522ce0f4bbbbaa6b3c38cafaa0f", "fake.tld"),
    Some(User("USARIO1", Some(Geo(Some("LT"))))),
    Some(Device("440579f4b408831516ebd02f6e1c31b4", Some(Geo(Some("LT"))))))
  val getBidRequestEntity: Future[RequestEntity] = Marshal(bidRequest).to[RequestEntity]

  override def beforeAll() {
    startServer()
  }

  override def afterAll() {
    stopServer()
  }

  behavior of "bid.BidAPITest"

  it should "handle BidRequest" in {
    val responseActorTestProbe = sendRequestAndGetResultResponseTestProbe(BID_REQUEST, HttpMethods.POST, Some(getBidRequestEntity))
    val httpResponse = responseActorTestProbe.receiveOne(systemDuration).asInstanceOf[HttpResponse]
    httpResponse.status shouldBe StatusCodes.OK

    val bidResponse = Await.result(Unmarshal(httpResponse).to[BidResponse], systemDuration)
    bidResponse shouldBe expectedBidResponse
  }
}
