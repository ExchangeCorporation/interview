package forex.services.oneforge

import java.time.OffsetDateTime

import forex.domain._
import monix.eval.Task
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.addon.monix.task._

object Interpreters {
  def dummy[R](
      implicit
      m1: _task[R]
  ): Algebra[Eff[R, ?]] = new Dummy[R]
}

final class Dummy[R] private[oneforge] (
    implicit
    m1: _task[R]
) extends Algebra[Eff[R, ?]] {
  override def get(
      pairs: Set[Rate.Pair]
  ): Eff[R, Error Either Set[Rate]] = {
    for {
      result <- fromTask(Task.now(
        pairs.map(pair => Rate(pair, Price(BigDecimal(100)), Timestamp.now))
      ))
    } yield Right(result)
  }
}
