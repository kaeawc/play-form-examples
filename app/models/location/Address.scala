package models

import play.api.libs.json.Json

case class Address(
  id         : Long,
  street     : StreetAddress,
  city       : City,
  state      : State,
  country    : Country,
  postalCode : PostalCode
)

object Address extends ((
  Long,
  StreetAddress,
  City,
  State,
  Country,
  PostalCode
) => Address) {

  implicit val r = Json.reads[Address]
  implicit val w = Json.writes[Address]

}
