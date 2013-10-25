package models

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

import play.api.libs.json.JsObject

trait Select[Model] {

  val table:String

  def count(where:Seq[(String,String)]):Future[Option[Long]]

  def exists(fields:Seq[(String,String)]):Future[Boolean] = {
    count(fields).map {
      result =>
      if (result.isEmpty) false
      else result.get > 0
    }
  }

  def getById(id:Long):Future[Option[Model]]

}