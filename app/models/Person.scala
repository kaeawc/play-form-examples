package models

import anorm._
import anorm.SqlParser._

import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._

import java.util.Date
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

case class Person(
  id      : Long,
  email   : String,
  created : Date
) extends User(email) 

case class PartialPerson(
  id      : Option[Long],
  email   : Option[String],
  created : Option[Date]
)

object Person extends ((
  Long,
  String,
  Date
) => Person) with Select[Person] {

  implicit val r = Json.reads[Person]
  implicit val w = Json.writes[Person]

  val table = "account"

  val people =
    long("id") ~
    str("email") ~
    date("created") map {
      case     id~email~created =>
        Person(id,email,created)
    }

  def getById(id:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            a.id,
            a.email,
            a.created
          FROM account a
          WHERE id = {id};
        """
      ).on(
        'id -> id
      ).as(people.singleOpt)
    }
  }

  def getByEmail(email:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            a.id,
            a.email,
            a.created
          FROM account a
          WHERE email = {email};
        """
      ).on(
        'email -> email
      ).as(people.singleOpt)
    }
  }

  def count(fields:Seq[(String,String)]) = Future {
    DB.withConnection { implicit connection =>

      val email = fields.foldLeft("") {
        (str,field) =>
        val (key,value) = field

        if (key == "email")
          value
        else
          ""
      }

      val result = SQL(
        """
          SELECT COUNT(1) count
          FROM account a
          WHERE email = {email};
        """
      ).on(
        'email -> email
      ).apply()

      try {
        Some(result.head[Long]("count"))
      } catch {
        case e:Exception => None
      }
    }
  }

  def whereEmailIs(email:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        s"""
          SELECT
            a.id,
            a.email,
            a.created
          FROM account a
          WHERE email = $email;
        """
      ).as(people *)
    }
  }

  def whereEmailIsLike(email:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        s"""
          SELECT
            a.id,
            a.email,
            a.created
          FROM account a
          WHERE email LIKE '%' || $email || '%';
        """
      ).as(people *)
    }
  }
}
