import play.api._

import scala.concurrent.duration._
import scala.concurrent.{Future,Await,ExecutionContext}
import ExecutionContext.Implicits.global

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    // Logger.info("Application has started")

    // Logger.info("Evolutions started")

    // Await.result(Future.sequence(Account.ups), 5 seconds)

    // Logger.info("Evolutions finished")

  }

  override def onStop(app: Application) {
    // Logger.info("Application shutdown...")
  }

}