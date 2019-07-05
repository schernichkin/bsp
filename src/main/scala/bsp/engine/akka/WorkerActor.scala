package bsp.engine.akka

import akka.actor._
import bsp.WorkerEnv

class WorkerActor extends Actor {

  override def receive: Receive = {
    case Run(task) => {
      val senderRef = sender()
      task(WorkerEnv(), {
        case Right(a) => senderRef ! a
        case Left(e) => senderRef ! Status.Failure(e)
      })
    }
  }
}