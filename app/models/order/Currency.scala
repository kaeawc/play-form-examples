package models.order

import models.{Enum,EnumJson}

import play.api.libs.json.Json

sealed trait Currency

case object USD extends Currency
case object CAN extends Currency
case object EUR extends Currency

object Currency extends Enum[Currency] with EnumJson[Currency] {
  val values = List(USD,CAN,EUR)
}
