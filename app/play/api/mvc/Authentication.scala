package play.api.mvc

import models.User

import play.api.Logger

import play.api.mvc.Results._
import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global
import play.api.templates.Html

trait Authentication extends State {

  /**
   * If request has authenticated user state do action A, otherwise do action B
   */
  def IfLoggedIn[A](
    a: Request[A] => Future[SimpleResult],
    b: Request[A] => Future[SimpleResult] = { implicit request:Request[A] => Future { deny() } }
  )(implicit request:Request[A]):Future[SimpleResult] =
    userState[A] flatMap {
      case Some(user:User) => a(request)
      case _               => b(request)
    }

  /**
   * If request has authenticated user state do action A, otherwise do action B
   */
  def IfLoggedInPage[A](
    a: Request[A] => Future[SimpleResult],
    b: Request[A] => Future[SimpleResult] = { implicit request:Request[A] => Future { Unauthorized(views.html.error.denied()) } }
  )(implicit request:Request[A]):Future[SimpleResult] =
    userState[A] flatMap {
      case Some(user:User) => a(request)
      case _               => b(request)
    }
}
