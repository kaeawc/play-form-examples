package models

import play.api.libs.json.Json

case class OrderItem(
  id       : Long,
  name     : String,
  cost     : Float,
  quantity : Long
)

object OrderItem extends ((
  Long,
  String,
  Float,
  Long
) => OrderItem) {

  implicit val r = Json.reads[OrderItem]
  implicit val w = Json.writes[OrderItem]

}
