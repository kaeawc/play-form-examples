package models

import java.util.Date

import play.api.libs.json.Json

case class UserGroup(
  id      : Long,
  name    : String,
  created : Date
)

object UserGroup extends ((
  Long,
  String,
  Date
) => UserGroup) {

  implicit val r = Json.reads[UserGroup]
  implicit val w = Json.writes[UserGroup]

}
