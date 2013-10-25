package models

import play.api.libs.json.Json

case class PostalCode(
  id   : Long,
  name : String
)

object PostalCode extends ((
  Long,
  String
) => PostalCode) {

  implicit val r = Json.reads[PostalCode]
  implicit val w = Json.writes[PostalCode]

}
