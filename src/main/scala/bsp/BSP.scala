package bsp

import cats._
import cats.data._
import cats.implicits._
import cats.kernel._

import scala.collection._

sealed trait BSP[F[_], A]

final case class Step[F[_], A](next: F[BSP[F, A]]) extends BSP[F, A]

final case class Done[F[_], A](result: F[A]) extends BSP[F, A]

object Stepper {
  def run[F[_] : Monad, A](bsp: BSP[F, A]): F[A] =
    bsp tailRecM {
      case Step(next) => next map Left.apply
      case Done(result) => result map Right.apply
    }
}

object ListBSP {

  final private[bsp] class FromSeqPartiallyApplied[F[_]](private val dummy: Boolean = true) extends AnyVal {
    def apply[L[_], A](xs: L[A])(implicit F: Applicative[F], L: Reducible[L], A: CommutativeSemigroup[A]): BSP[F, A] = Done(xs.reduce.pure[F])
  }

  def fromSeq[F[_]]: FromSeqPartiallyApplied[F] = new FromSeqPartiallyApplied[F]
}

final class ListBSPOps[L[_], A](private val l: L[A]) extends AnyVal {
  def toProcess[F[_]](implicit F: Applicative[F], L: Reducible[L], A: CommutativeSemigroup[A]): BSP[F, A] = ListBSP.fromSeq[F](l)
}

trait ListBSPSyntax {
  implicit final def listBSPOps[L[_], A](a: L[A]): ListBSPOps[L, A] = new ListBSPOps[L, A](a)
}

object TestListBSP extends ListBSPSyntax {
  def main(args: Array[String]): Unit = {
    val process = NonEmptyList.of(1, 2, 3).toProcess[Id]
    println(Stepper.run(process))
  }
}

trait VertexProgram[M, A] {
  def init(): Iterator[(Long, M)]

  def process(msg: M, attr: Option[A]): (Option[A], Iterator[(Long, M)])
}

// State level:
// - vertex          [private]
// - worker thread   [shared] [thread-safe]
// - NUMA node       []
// - machine
// - rack
// - datacenter
// - global

object SingleWorkerBSP {
  final private[bsp] class PregelPartiallyApplied[F[_]](private val dummy: Boolean = true) extends AnyVal {
    def apply[M, A](init: Iterator[(Long, M)], process: (M, Option[A]) => (Option[A], Iterator[(Long, M)]))
                   (implicit F: Applicative[F], M: CommutativeSemigroup[M], A: CommutativeSemigroup[A]): BSP[F, Option[A]] = {

      def collectMessages(map: mutable.Map[Long, M], messages: Iterator[(Long, M)]): mutable.Map[Long, M] = {
        messages.foreach { case (key, a) =>
          if (map.contains(key)) {
            map(key) = map(key) |+| a
          } else
            map(key) = a
        }
        map
      }

    //  val attributes = mutable.HashMap[Long, A]

      // val attributes = mutable.HashMap[]

      // def loop(inMessages: Map[Long, M])

      Step(F.pure {
      //  val messages = collectMessages(mutable.HashMap[Long, A](), init)
        Done(F.pure(None))
      })
    }
  }

}