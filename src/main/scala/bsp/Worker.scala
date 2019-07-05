package bsp

import simulacrum.typeclass

/**
  * Интерфейс для коммуникации драйвера с воркером.
  */
@typeclass trait Worker[F[_]] {
  /**
    * Запустить воркер и ждать результат.
    * @return
    */
  def run[G[_]: Runnable, A](g: WorkerT[G, A]): F[A]
}
