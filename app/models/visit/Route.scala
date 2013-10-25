package models.visit

import java.util.Date

import play.api.libs.json.Json

case class Route(
  id      : Long,
  name    : String,
  created : Date
)

object Route extends ((
  Long,
  String,
  Date
) => Route) {

  implicit val r = Json.reads[Route]
  implicit val w = Json.writes[Route]

}
