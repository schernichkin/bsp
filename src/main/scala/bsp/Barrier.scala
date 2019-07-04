package bsp

import java.io._

import cats._
import cats.implicits._
import cats.data._
import cats.effect._
import simulacrum._
import monix.eval._
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Await
import scala.concurrent.duration._

@typeclass trait Barrier[F[_]] {
  def barrier: F[Unit]
}

object ReaderTInstance {
  implicit def readerTInstance[F[_]: Applicative]: Barrier[ReaderT[F, PeerState, ?]] = new Barrier[ReaderT[F, PeerState, ?]] {
    override def barrier: ReaderT[F, PeerState, Unit] = ().pure[ReaderT[F, PeerState, ?]]
  }
}


case class PeerState()

object TestMe {
  import ReaderTInstance._

  def program[F[_] : Barrier] = Barrier[F].barrier

  def main(args: Array[String]): Unit = {
    val aa = program[ReaderT[Task, PeerState, ?]]

    val stream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(aa)

    val in = new ByteArrayInputStream(stream.toByteArray)
    val ois = new ObjectInputStream(in)

    val ddd = ois.readObject().asInstanceOf[ReaderT[Task, PeerState, Unit]]

    val task = ddd.run(new PeerState)
    val res = Await.result(task.runToFuture, 5.seconds)

    println(res)
  }
}