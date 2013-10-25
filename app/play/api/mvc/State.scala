package play.api.mvc

import models._
import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

trait State {

  val noFuture = Future { None }

  /**
   * Attempts to get user metadata.
   */
  implicit def userState[A](implicit request: Request[A]):Future[Option[User]] = {
    state[A] flatMap {
      case Some(visit:Visit) => {
        visit.account match {
          case Some(id:Long) => Account.getById(id)
          case _ => noFuture
        }
      }
      case _ => noFuture
    }
  }

  /**
   * Gets a user implicitly from request 
   */
  implicit def getUser(implicit request: Request[AnyContent]):Future[User] =
    userState[AnyContent] map {
      case Some(user:User) => user
      case _ => throw new Exception("Failed to get current user.")
    }

  /**
   * Gets a client visit state
   * @type {[type]}
   */
  implicit def getState(implicit request: Request[AnyContent]):Future[Visit] =
    state[AnyContent] map {
      case Some(visit:Visit) => visit
      case _ => throw new Exception("Failed to get visit state.")
    }

  /**
   * Attempts to parse the request implicitly for a client UserView
   * @type {[type]}
   */
  implicit def state[A](implicit request: Request[A]):Future[Option[Visit]] =
    Visit.create(request)

}
