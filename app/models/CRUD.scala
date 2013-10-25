package models

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

import play.api.libs.json.JsObject

trait CRUD[Model,Assembly] {

  def find(where:Map[String,String]):Future[List[Model]]

  def update(where:Map[String,String],to:Map[String,String]):Future[List[Model]]

  def delete(id:Long):Future[Option[Model]]

  def create(data:AnyVal *):Future[Option[Model]]

}