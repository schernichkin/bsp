package bsp.engine.akka

import akka.actor.Actor

class DriverActor extends Actor {
  override def receive: Receive = {
    case Error(e) => ???
    case Completed => ???
  }
}