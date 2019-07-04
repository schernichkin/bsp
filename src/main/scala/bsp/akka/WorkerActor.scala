package bsp.akka

import akka.actor._
import akka.pattern._

import scala.concurrent.{ExecutionContext, Future}

sealed trait WorkerMessage

object Run extends WorkerMessage

abstract class WorkerActor[G[_]](
                                  id: Int,
                                  driver: ActorRef,
                                  program: G[Unit]
                                )
                                (
                                  implicit context: ExecutionContext
                                ) extends Actor {
  override def preStart(): Unit = {
    println(s"=== worker $id preStart")
  }

  override def postStop(): Unit = {
    println(s"=== worker $id postStop")
  }

  override def receive: Receive = {
    case Run => ???
  }
}
