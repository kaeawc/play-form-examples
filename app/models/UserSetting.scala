package models

import java.util.Date

import play.api.libs.json.Json

case class UserSetting(
  id      : Long,
  name    : String,
  created : Date
)

object UserSetting extends ((
  Long,
  String,
  Date
) => UserSetting) {

  implicit val r = Json.reads[UserSetting]
  implicit val w = Json.writes[UserSetting]

}
