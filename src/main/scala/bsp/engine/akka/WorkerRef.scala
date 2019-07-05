package bsp.engine.akka

import java.io._

import akka.actor._
import akka.pattern._
import akka.util.Timeout
import bsp.{FromFuture, Runnable, Worker, WorkerEnv, WorkerT}

import scala.concurrent.ExecutionContext

case class WorkerRef[F[_] : FromFuture](
                                         workerRef: ActorRef
                                       )(
                                         implicit timeout: Timeout, executionContext: ExecutionContext
                                       ) extends Worker[F] {

  def testSerialization(a: AnyRef): Unit = {
    val stream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(a)
    println(s"==== program size: ${stream.size()}")
    val in = new ByteArrayInputStream(stream.toByteArray)
    val ois = new ObjectInputStream(in)
    ois.readObject()
  }

  /**
    * Запустить воркер и ждать результат.
    *
    * @return
    */
  override def run[G[_] : Runnable, A](f: WorkerT[G, A]): F[A] = {
    val task: (WorkerEnv, Either[Throwable, A] => Unit) => Unit = (env, cb) => Runnable[G].runAsync(f(env), cb)

    testSerialization(task)
    FromFuture[F].fromFuture {
      workerRef ask Run(task) map {
        _.asInstanceOf[A]
      }
    }
  }
}
