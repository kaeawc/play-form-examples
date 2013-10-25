package controllers

import play.api.Logger
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext,Future}
import ExecutionContext.Implicits.global

import models.Account

object Accounts extends Controller with FormBinding {

  def get(id:Long) = Action.async {
    Account.getById(id) map {
      case Some(account:Account) => Ok(Json.toJson(account))
      case _ => NotFound
    }
  }

  def getByEmail(email:String) = Action.async {
    Account.getByEmail(email) map {
      case Some(account:Account) => Ok(Json.toJson(account))
      case _ => NotFound
    }
  }

  val authForm = Form(
    tuple(
      "email"    -> email,
      "password" -> text(minLength = 6)
    )
  )

  val emailForm = Form(
    "email"    -> email
  )

  val passwordForm = Form(
    "password" -> text(minLength = 6)
  )

  def create = FormAction(authForm) {
    tuple:(String,String) =>

    val (email:String,password:String) = tuple

    Account.create(email,password) map {
      case Some(account:Account) =>
        Created(Json.toJson(account))
      case _ =>
        NotFound
    }
  }

  def updateEmail(id:Long) = FormAction(emailForm) {
    email:String =>

    Account.getByEmail(email) flatMap {
      case Some(other:Account) => Future {
        Conflict(Json.obj("other" -> Json.obj("id" -> other.id)))
      }
      case _ => {
        Account.updateEmail(id,email) map {
          case Some(account:Account) =>
            Accepted(Json.obj("id" -> account.id, "email" -> "updated"))
          case _ =>
            NotFound
        }
      }
    }
  }

  def updatePassword(id:Long) = FormAction(passwordForm) {
    password:String =>

    Account.updatePassword(id,password) map {
      case Some(account:Account) =>
        Accepted(Json.obj("id" -> account.id, "password" -> "updated"))
      case _ =>
        NotFound
    }
  }

  def remove(id:Long) = Action.async {
    Account.delete(id) map {
      case Some(account:Account) =>
        Accepted(Json.obj("id" -> account.id))
      case _ =>
        NotFound
    }
  }
}
