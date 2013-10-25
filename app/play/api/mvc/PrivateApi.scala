package play.api.mvc

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

trait PrivateApi extends Authentication {

  /**
   * Action checks for authenticated user state
   */
  def LoggedIn(f: Request[AnyContent] => Future[SimpleResult]) =
    Action.async { implicit request => IfLoggedIn[AnyContent](f) }

  /**
   * Action checks for authenticated user state
   */
  def LoggedIn[A](bp: BodyParser[A])(f: Request[A] => Future[SimpleResult]) =
    Action.async(bp) { implicit request => IfLoggedIn[A](f) }

}
