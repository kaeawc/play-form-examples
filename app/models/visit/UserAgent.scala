package models.visit

import java.util.Date

import play.api.libs.json.Json

case class UserAgent(
  id      : Long,
  agent   : String,
  created : Date
)

object UserAgent extends ((
  Long,
  String,
  Date
) => UserAgent) {

  implicit val r = Json.reads[UserAgent]
  implicit val w = Json.writes[UserAgent]

}
