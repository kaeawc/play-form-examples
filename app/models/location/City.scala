package models

import play.api.libs.json.Json

case class City(
  id   : Long,
  name : String
)

object City extends ((
  Long,
  String
) => City) {

  implicit val r = Json.reads[City]
  implicit val w = Json.writes[City]

}
