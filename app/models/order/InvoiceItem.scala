package models

import play.api.libs.json.Json

case class InvoiceItem(
  id       : Long,
  name     : String,
  cost     : Float,
  quantity : Long
)

object InvoiceItem extends ((
  Long,
  String,
  Float,
  Long
) => InvoiceItem) {

  implicit val r = Json.reads[InvoiceItem]
  implicit val w = Json.writes[InvoiceItem]

}
