package models.visit

import java.util.Date

import play.api.libs.json.Json

case class OperatingSystem(
  id      : Long,
  name    : String,
  version : String,
  created : Date
)

object OperatingSystem extends ((
  Long,
  String,
  String,
  Date
) => OperatingSystem) {

  implicit val r = Json.reads[OperatingSystem]
  implicit val w = Json.writes[OperatingSystem]

}
