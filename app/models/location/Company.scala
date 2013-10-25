package models

import play.api.libs.json.Json

case class Company(
  id      : Long,
  name    : String,
  address : Address
)

object Company extends ((
  Long,
  String,
  Address
) => Company) {

  implicit val r = Json.reads[Company]
  implicit val w = Json.writes[Company]

}
