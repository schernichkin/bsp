package bsp

import simulacrum.typeclass

/**
  * Интерфейс для коммуникации воркера с драйвером.
  */
//TODO: Он не нужен. Мы просто выполняем программу и возвращаем результат.
@typeclass trait Driver[F[_]] {
  /**
    * Информировать драйвер об окончании работы программы.
    */
  def cancelled: F[Unit]

  def completed: F[Unit]

  /**
    * Отправить драйверу информацию об ошибке.
    */
  def error(e: Throwable): F[Unit]
}