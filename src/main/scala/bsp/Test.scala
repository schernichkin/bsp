package bsp

import akka.actor._
import akka.util.Timeout
import bsp.engine.akka._
import bsp.engine.monix.implicits._
import cats.effect._
import cats.implicits._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.duration._


object Test {
  def workerProgram[F[_] : LiftIO](implicit Bracket: Bracket[F, Throwable]): F[Unit] = io {
    println("Hello World!")
  }

  def program[F[_] : Process : Concurrent : Runnable](implicit Bracket: Bracket[F, Throwable]): F[Unit] =
    Process[F].workers.use { workers =>
      for {
        fs <- workers.toList.traverse { w =>
          Concurrent[F].start(w.run{ io[WorkerT[F, ?], Unit] {
            println("Hello World!")
          } })
        }
        _ <- fs.traverse {
          _.join
        }
      } yield ()
    }


  def main(args: Array[String]): Unit = {
    implicit val timeout: Timeout = Timeout(5 seconds)
    implicit val actorSystem: ActorSystem = ActorSystem.create()
    implicit val process: LocalProcess[Task] = LocalProcess()

    val p = program[Task]

    println("=== running program")

    //   val a : IO[Unit]

    //   a.runAsync()


    //  p.runAsync(???)
    try {
      val res = p.runSyncUnsafe()
    }
    finally {
      println("=== done. terminating.")
      actorSystem.terminate()
    }
  }
}
