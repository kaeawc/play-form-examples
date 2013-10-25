package models

import play.api.libs.json.Json

case class Country(
  id   : Long,
  name : String
)

object Country extends ((
  Long,
  String
) => Country) {

  implicit val r = Json.reads[Country]
  implicit val w = Json.writes[Country]

}
