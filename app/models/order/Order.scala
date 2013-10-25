package models

import java.util.Date
import play.api.libs.json.Json

case class Order(
  id         : Long,
  from       : Company,
  to         : Company,
  items      : List[OrderItem],
  created    : Date,
  signatures : Map[String,Date]
)

object Order extends ((
  Long,
  Company,
  Company,
  List[OrderItem],
  Date,
  Map[String,Date]
) => Order) {

  implicit val r = Json.reads[Order]
  implicit val w = Json.writes[Order]

}
