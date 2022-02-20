package main

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import controller.Controller

import scala.concurrent.ExecutionContextExecutor

object BiddingSystem extends App {
  implicit val system = ActorSystem("BiddingSystem")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  system.actorOf(Controller.props(), "controller")
}
