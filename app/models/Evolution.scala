package models

import play.api.Logger
import scala.concurrent.Future

trait Evolution[Model,Assembly] extends CRUD[Model,Assembly] {

  val evolutions:List[Assembly]

  // def up(item:Assembly):Future[Assembly] =
  //   create(item).map {
  //     result =>
  //     if (result.isDefined)
  //       Some(item)
  //     else
  //       None
  //   }

  // def down(item:Assembly):Future[Assembly] =
  //   getByEmail(email).map {
  //     account =>
  //     if (!account.isEmpty)
  //       delete(account.get.id)

  //     (email,password)
  //   }
  // }

  // def ups:List[Future[Assembly]] = {
  //   evolutions map { item =>
  //     // Logger.info("Evolving Item")
  //     up(item)
  //   }
  // }

  // def downs:List[Future[Assembly]] = {
  //   evolutions map { item =>
  //     // Logger.info("Evolving Item")
  //     down(item)
  //   }
  // }

}
