package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def docs = Action {
    Ok(views.html.docs("Your new application is ready."))
  }
}
