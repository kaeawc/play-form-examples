package models.visit

import java.util.Date

import play.api.libs.json.Json

case class Browser(
  id      : Long,
  name    : String,
  version : String,
  created : Date
)

object Browser extends ((
  Long,
  String,
  String,
  Date
) => Browser) {

  implicit val r = Json.reads[Browser]
  implicit val w = Json.writes[Browser]

}
