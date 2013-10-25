package models

import play.api.libs.json.Json

case class State(
  id   : Long,
  name : String
)

object State extends ((
  Long,
  String
) => State) {

  implicit val r = Json.reads[State]
  implicit val w = Json.writes[State]

}
