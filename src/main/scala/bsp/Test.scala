package bsp

import java.io._

import bsp.akka.{AkkaDriver, LocalDriver}
import cats._
import cats.implicits._
import cats.data._
import cats.effect._
import simulacrum._
import monix.eval._
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration._
import _root_.akka.actor._

object Test {
  def program[F[_] : LiftIO]: F[Unit] = LiftIO[F].liftIO( IO { println("Hello World!") })

  def testSerialization(a: AnyRef): Unit = {
    val stream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(a)
    val in = new ByteArrayInputStream(stream.toByteArray)
    val ois = new ObjectInputStream(in)
    ois.readObject()
  }

  def main(args: Array[String]): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem.create()
    implicit val driver: LocalDriver[Task, Task] = LocalDriver[Task, Task](10)
    val p = program[Task]
    println("=== running program")
    AkkaDriver.run(p).runSyncUnsafe()
    println("=== done. terminating.")
    actorSystem.terminate()
  }
}
