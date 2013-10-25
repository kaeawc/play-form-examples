package play.api.mvc

import play.api.data.Form
import play.api.data.Forms._

import scala.concurrent.{ExecutionContext,Future}
import ExecutionContext.Implicits.global

trait FormBinding {

  def FormAction[Tuple]
    (form    : Form[Tuple])
    (success : Tuple => Future[SimpleResult]) =
  Action.async {
    implicit request => BindForm(form)(success)
  }

  def BindForm[Tuple](
    form    : Form[Tuple]
  )(
    success : Tuple => Future[SimpleResult]
  )(implicit request:Request[AnyContent]) = {
    form.bindFromRequest match {
      case form:Form[Tuple] if form.hasErrors =>
        Future {
          bad(form.errors.map {
            error =>
            error.key -> error.message
          })
        }
      case form:Form[Tuple] =>
        success(form.get)
    }
  }
}
