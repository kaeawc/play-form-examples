package models

import java.util.Date

import scala.concurrent.Future

abstract class User(
  email   : String,
  created : Date         = new Date()
) {

  def getEmail:String = email

}
