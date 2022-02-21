package util

import akka.actor.ActorRef
import akka.http.scaladsl.model._
import akka.testkit.TestProbe
import mockup.HTTPServerMockup.{m_http, m_serviceHost, m_servicePort, _}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object TestUtils {
  def sendRequestAndGetResultResponseTestProbe(path: String,
                                               method: HttpMethod,
                                               futureEntityOption: Option[Future[RequestEntity]]): TestProbe = {
    val testProbe = TestProbe()
    sendRequestAndSendResultToResponseActor(testProbe.ref, path, method, futureEntityOption)
    testProbe
  }

  def sendRequestAndSendResultToResponseActor(responseActor: ActorRef,
                                              path: String,
                                              method: HttpMethod,
                                              futureEntityOption: Option[Future[RequestEntity]] = None) {
    val response = futureEntityOption match {
      case Some(futureEntity) => futureEntity.flatMap({ entity => this.getRequest(path, method, Some(entity)) })
      case None => getRequest(path, method, None)
    }

    response.onComplete {
      case Success(httpResponse) =>
        responseActor ! httpResponse
      case Failure(requestFailure) =>
        requestFailure.printStackTrace()
    }
  }

  def getRequest(path: String,
                 method: HttpMethod,
                 entityOption: Option[RequestEntity]): Future[HttpResponse] = {
    val request =
      HttpRequest(
        method = method,
        headers = List(),
        uri = s"http://$m_serviceHost:$m_servicePort/$path",
        entity = entityOption.getOrElse(HttpEntity.Empty)
      )
    m_http.singleRequest(request)
  }
}
