package play.api

import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json._
import scala.language.implicitConversions

package object mvc {

  /**
   * Return a HTTP 400 JSON response
   */
  def bad(reasons:JsValue):SimpleResult = BadRequest(reasons)

  /**
   * Return a HTTP 401 JSON response
   */
  def deny(reasons:JsValue):SimpleResult = Unauthorized(reasons)
  
  /**
   * Return a HTTP 400 JSON response of the form {'reason':reason}
   */
  def bad(reason:String = "Could not process your request."):SimpleResult = bad(seqToJson(Seq("reason" -> reason)))
  
  /**
   * Return a HTTP 401 JSON response of the form {'reason':reason}
   */
  def deny(reason:String = "You are not logged in."):SimpleResult = deny(seqToJson(Seq("reason" -> reason)))
  
  /**
   * Return a HTTP 400 JSON response composed from the sequence collection
   */
  def bad(reasons:Seq[(String,String)]):SimpleResult = bad(seqToJson(reasons))
  
  /**
   * Return a HTTP 401 JSON response composed from the sequence collection
   */
  def deny(reasons:Seq[(String,String)]):SimpleResult = deny(seqToJson(reasons))

  implicit def kvToSeq(kv:(String,String)):JsValue = seqToJson(Seq(kv))
  implicit def seqToJson(seq:Seq[(String,String)]):JsValue = JsObject(seq.map { case(k,v) => (k,JsString(v))})

}
