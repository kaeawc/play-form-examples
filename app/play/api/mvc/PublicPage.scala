package play.api.mvc

import play.api.mvc.Results._
import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

trait PublicPage extends Authentication {

  /**
   * Action checks for authenticated user state
   */
  def OnlyPublic(f: Request[AnyContent] => Future[SimpleResult]) =
    Action.async { implicit request => IfLoggedIn[AnyContent]({ request => Future { Redirect("/") } }, f) }

  /**
   * Action checks for authenticated user state
   */
  def OnlyPublic[A](bp: BodyParser[A])(f: Request[A] => Future[SimpleResult]) =
    Action.async(bp) { implicit request => IfLoggedIn[A]({ request => Future { Redirect("/") } }, f) }
}
