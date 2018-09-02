package forex.http
package rates

import cats.effect.Sync
import cats.syntax.flatMap._
import forex.rates.GetRatesRequest
import forex.rates.algebra.Rates
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

class RatesHttpRoutes[F[_]: Sync](rates: Rates[F]) extends Http4sDsl[F] {

  import Converters._, QueryParams._, Protocol._

  private[http] val prefixPath = "/rates"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root :? FromQueryParam(from) :? ToQueryParam(to) =>
      rates.get(GetRatesRequest(from, to)).flatMap { rate =>
        Ok(rate.asGetApiResponse)
      }
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
