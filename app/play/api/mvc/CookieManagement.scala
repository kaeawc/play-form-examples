package play.api.mvc

import play.api.Logger
import play.api.libs.Crypto
import models._
import scala.concurrent.duration._
import scala.concurrent.Await

trait CookieManagement extends Configuration {

  /**
   * creates a Cookie instance with an encrypted value
   */
  def createCookie(key:String,value:String,secure:Boolean = false, rememberMe:Boolean = false) = {
    val expires:Option[Int] = if(rememberMe) Option(31536000) else None
    play.api.mvc.Cookie(key, Crypto.encryptAES(value), expires, "/", None, secure)
  }

  /**
   * reads a RequestHeader's Cookie by the given key if it exists
   */
  def readCookie(request:play.api.mvc.RequestHeader,key:String):String = {
    val cookie = request.cookies.get(key)
    cookie match {
      case Some(c:play.api.mvc.Cookie) => Crypto.decryptAES(c.value)
      case _ => throw new Exception("Could not read key [" + key + "] from cookie in request.")
    }
  }

  /**
   * attempts to decode the current user's cookie
   */
  def getUserFromCookie(request:play.api.mvc.RequestHeader):Option[User] = {  
    try {
      val cookie = readCookie(request,userCookieKey)
      Await.result(Account.getById(cookie.toLong), 5 seconds)
    } catch {
      case e:Exception => {
        Logger.error("Couldn't resolve User from Cookie.")
        Logger.error(e.getMessage)
        None
      }
    }
  }
}
