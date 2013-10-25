import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.Logger
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext}
import ExecutionContext.Implicits.global

class AccountSpec extends Specification {

  import models.Account
  import models.Account._

  "getById" should {

    "return an Account if one exists" in new WithApplication {

      val (email,password) = ("someone@example.com","password")

      create(email,password).map {
        case Some(account:Account) => {

          val request = FakeRequest(GET, "/account/" + account.id)

          val response = route(request).get

          status(response) must equalTo(OK)
          contentType(response) must beSome("application/json")

          parse(contentAsString(response)) match {
            case Account(id,email,hashedPassword,hashSalt,created) => {
              account.email mustEqual email
              account.password mustEqual hashedPassword
              useSalt(password,hashSalt) mustEqual hashedPassword
            }
            case _ => failure("Could not parse the response as an Account object")
          }
        }
        case _ => failure("Failed to create account for test.")
      }
    }

    "return NotFound if the account doesn't exist" in new WithApplication {

      val request = FakeRequest(GET, "/account/-5")

      val response = route(request).get

      status(response) must equalTo(404)
      contentType(response) must beNone
    }
  }

  "getByEmail" should {

    "return an Account if one exists" in new WithApplication {

      val (email,password) = ("someone@example.com","password")

      create(email,password).map {
        case Some(account:Account) => {
          val request = FakeRequest(GET, "/account/by/email/someone@example.com")

          val response = route(request).get

          status(response) must equalTo(OK)
          contentType(response) must beSome.which(_ == "application/json")

          parse(contentAsString(response)) match {
            case Account(id,email,password,salt,created) => {
              account.email mustEqual email
              account.password mustEqual password
            }
            case _ => failure("Could not parse the response as an Account object")
          }
        }
        case _ => failure("Failed to create account for test.")
      }
    }

    "return NotFound if account doesn't exist" in new WithApplication {

      val request = FakeRequest(GET, "/account/by/email/something@nowhere.com")

      val response = route(request).get

      status(response) must equalTo(404)
      contentType(response) must beNone
    }
  }


  "create" should {

    "return Accepted if creation was performed correctly" in new WithApplication {

      val header = FakeRequest(POST, "/account")

      val data = Json.obj("email" -> "someone@example.com", "password" -> "password")

      val response = route(header,data).get

      status(response) must equalTo(201)
      contentType(response) must beSome("application/json")

      parse(contentAsString(response)) match {
        case account:Account => {
          account.email mustEqual "someone@example.com"

          account.password mustEqual useSalt("password",account.salt)
        }
        case _ => failure("Could not parse the response as an Account object")
      }
    }

    "return BadRequest if not all information was sent." in new WithApplication {

      val request = FakeRequest(POST, "/account")

      val response = route(request).get

      status(response) must equalTo(400)
      contentType(response) must beSome("application/json")
      contentAsString(response) mustEqual(Json.obj("email" -> "error.required","password" -> "error.required").toString)
    }
  }

  "updateEmail" should {

    "return Accepted if update was performed correctly" in new WithApplication {

      val (email,password) = ("someone@example.com","password")

      create(email,password).map {
        case Some(account:Account) => {

          val header = FakeRequest(PUT, "/account/" + account.id + "/change/email")

          val data = Json.obj("email" -> "someone-else@example.com")

          val response = route(header,data).get

          status(response) must equalTo(202)
          contentType(response) must beNone
        }
        case _ => {
          failure("Failed to create count, can't perform test.")
        }
      }
    }

    "return BadRequest if some of the parameters are missing" in new WithApplication {

      val (email,password) = ("someone@example.com","password")

      create(email,password).map {
        case Some(account:Account) => {

          val request = FakeRequest(PUT, "/account/" + account.id + "/change/email")

          val response = route(request).get

          status(response) must equalTo(400)
          contentType(response) must beNone
        }
        case _ => {
          failure("Failed to create count, can't perform test.")
        }
      }
    }

    "return NotFound if the account doesn't exist" in new WithApplication {

      val header = FakeRequest(PUT, "/account/-5/change/email")

      val data = Json.obj("email" -> "someone-else@example.com")

      val response = route(header,data).get

      status(response) must equalTo(404)
      contentType(response) must beNone
    }
  }

  "updatePassword" should {

    "return Accepted if update was performed correctly" in new WithApplication {


      val (email,password) = ("someone@example.com","password")

      create(email,password).map {
        case Some(account:Account) => {

          val header = FakeRequest(PUT, "/account/" + account.id + "/change/password")

          val data = Json.obj("password" -> "new password")

          val response = route(header,data).get

          status(response) must equalTo(202)
          contentType(response) must beNone
        }
        case _ => {
          failure("Failed to create count, can't perform test.")
        }
      }
    }

    "return NotFound if none exist" in new WithApplication {


      val (email,password) = ("someone@example.com","password")

      val head = FakeRequest(PUT, "/account/-5/change/password")
      val body = Json.obj("password" -> password)

      val response = route(head,body).get

      status(response) must equalTo(404)
      contentType(response) must beNone
    }
  }

  "remove" should {

    "return Accepted if deletion was performed correctly" in new WithApplication {


      val (email,password) = ("someone@example.com","password")

      create(email,password).map {
        case Some(account:Account) => {
          val request = FakeRequest(GET, "/account/" + account.id)

          val response = route(request).get

          status(response) must equalTo(201)
          contentType(response) must beNone
        }
        case _ => {
          failure("Failed to create count, can't perform test.")
        }
      }
    }

    "return NotFound if account doesn't exist" in new WithApplication {

      val request = FakeRequest(GET, "/account/-5")

      val response = route(request).get

      status(response) must equalTo(404)
      contentType(response) must beNone
    }
  }
}
