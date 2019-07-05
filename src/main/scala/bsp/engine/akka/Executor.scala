package bsp.engine.akka

object Executor {
  def run[F[_]](task: F[_]): Unit = {

    // 1. создаём драйвер.
    // 2. coздаём воркеры.
    // 3. запускаем воркеры.
    // 4. ждём результата.
    ???
  }
}
