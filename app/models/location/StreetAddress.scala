package models

import play.api.libs.json.Json

case class StreetAddress(
  id   : Long,
  name : String
)

object StreetAddress extends ((
  Long,
  String
) => StreetAddress) {

  implicit val r = Json.reads[StreetAddress]
  implicit val w = Json.writes[StreetAddress]

}
