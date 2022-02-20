package controller

import akka.actor.{Actor, Props}
import model.ModelWorker
import view.ViewWorker

object Controller {
  def props(): Props = Props(new Controller)
}

class Controller extends Actor {
  val m_viewWorker = context.actorOf(ViewWorker.props(self), "view-worker")
  val m_modelWorker = context.actorOf(ModelWorker.props(self), "model-worker")

  val m_systemName: String = context.system.name
  val m_modelWorkerActorPath = s"akka://$m_systemName/user/controller/model-worker"
  val m_viewWorkerActorPath = s"akka://$m_systemName/user/controller/view-worker"

  override def receive: Receive = {
    case message if sender.path.toString.startsWith(m_modelWorkerActorPath) =>
      m_viewWorker ! message
    case message if sender.path.toString.startsWith(m_viewWorkerActorPath) =>
      m_modelWorker ! message
  }
}
