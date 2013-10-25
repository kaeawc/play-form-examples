package models

import java.util.Date
import play.api.libs.json.Json

case class Invoice(
  id         : Long,
  from       : Company,
  to         : Company,
  items      : List[InvoiceItem],
  created    : Date,
  signatures : Map[String,Date]
)

object Invoice extends ((
  Long,
  Company,
  Company,
  List[InvoiceItem],
  Date,
  Map[String,Date]
) => Invoice) {

  implicit val r = Json.reads[Invoice]
  implicit val w = Json.writes[Invoice]

}
