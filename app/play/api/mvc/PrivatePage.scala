package play.api.mvc

import play.api.mvc.Results._
import play.api.templates.Html

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

trait PrivatePage extends Authentication {

  /**
   * Action checks for authenticated user state
   */
  def LoggedInPage(f: Request[AnyContent] => Future[SimpleResult]) =
    Action.async { implicit request => IfLoggedIn[AnyContent](f,{ request => Future { Redirect("/") } }) }

  /**
   * Action checks for authenticated user state
   */
  def LoggedInPage[A](bp: BodyParser[A])(f: Request[A] => Future[SimpleResult]) =
    Action.async(bp) { implicit request => IfLoggedInPage[A](f,{ request => Future { Redirect("/") } }) }
}
