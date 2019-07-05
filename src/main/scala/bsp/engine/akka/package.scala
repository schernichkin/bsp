package bsp.engine

import bsp._

package object akka {
  type WorkerTask[A] = (WorkerEnv, Either[Throwable, A] => Unit) => Unit
}
