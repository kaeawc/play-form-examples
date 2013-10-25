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

case class Account(
  id       : Long,
  email    : String,
  password : String,
  salt     : String,
  created  : Date = new Date()
) extends User(email) with Credentials {

  def updateEmail(newEmail:String) =
    Account.updateEmail(id,newEmail)

  def updatePassword(newPassword:String) =
    Account.updatePassword(id,newPassword)

}

case class PartialAccount(
  id       : Option[Long],
  email    : Option[String],
  password : Option[String],
  salt     : Option[String],
  created  : Option[Date]
)

case class AccountAssembly(
  email    : String,
  password : String
)

object Account extends ((
  Long,
  String,
  String,
  String,
  Date
) => Account)
with Authentication
with CRUD[Account,AccountAssembly] {

  implicit val r = Json.reads[Account]
  implicit val w = Json.writes[Account]

  def parse(json:String) = 
    Json.fromJson(Json.parse(json)).get

  val table = "account"

  val accounts =
    long("id") ~
    str("email") ~
    str("password") ~
    str("salt") ~
    date("created") map {
      case      id~email~password~salt~created =>
        Account(id,email,password,salt,created)
    }

  def getById(id:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            a.id,
            a.email,
            a.password,
            a.salt,
            a.created
          FROM account a
          WHERE id = {id};
        """
      ).on(
        'id -> id
      ).as(accounts.singleOpt)
    }
  }

  def getByEmail(email:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            a.id,
            a.email,
            a.password,
            a.salt,
            a.created
          FROM account a
          WHERE email = {email};
        """
      ).on(
        'email -> email
      ).as(accounts.singleOpt)
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

  def exists(fields:Seq[(String,String)]) = {
    count(fields).map {
      result =>
      if (result.isEmpty) false
      else result.get > 0
    }
  }

  def whereEmailIs(email:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        s"""
          SELECT
            a.id,
            a.email,
            a.password,
            a.salt,
            a.created
          FROM account a
          WHERE email = $email;
        """
      ).as(accounts *)
    }
  }

  def whereEmailIsLike(email:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        s"""
          SELECT
            a.id,
            a.email,
            a.password,
            a.salt,
            a.created
          FROM account a
          WHERE email LIKE '%' || $email || '%';
        """
      ).as(accounts *)
    }
  }

  def create(data:AnyVal *) = {


    val email            = data(0)
    val password         = data(1)
    val salt             = createSalt
    val created          = new Date()
    val hashedPassword   = useSalt(password.toString,salt)
    val storedSalt       = new String(salt.map(_.toChar))

    Future {
      
      DB.withConnection { implicit connection =>
        SQL(
          """
            INSERT INTO account (
              email,
              password,
              salt,
              created
            ) VALUES (
              {email},
              {password},
              {salt},
              {created}
            );
          """
        ).on(
          'email    -> email.toString,
          'password -> hashedPassword,
          'salt     -> storedSalt,
          'created  -> created
        ).executeInsert()
      }
    }.map {
      id =>

      if (id.isDefined)
        Some(Account(
          id.get,
          email.toString,
          hashedPassword,
          storedSalt,
          created
        ))
      else
        None
    }
  }

  def find(where:Map[String,String]) = {
    if (where.get("email").isDefined)
      findByEmail(where("email"))
    else
      Future { Nil }
  }

    

  def findByEmail(email:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            a.id,
            a.email,
            a.password,
            a.salt,
            a.created
          FROM account a
          WHERE email = {email};
        """
      ).on(
        'email -> email
      ).as(accounts *)
    }
  }

  def update(
    where:Map[String,String],
    to:Map[String,String]
  ) =
    if (
      to.get("email").isDefined ||
      to.get("password").isDefined
    )
      find(where) map {
        case accounts:List[Account] => {
          accounts map {
            account:Account =>
            
            val updated =
            if (to.get("email").isDefined)
              updateEmail(
                account.id,
                to("email")
              )
            else
              updatePassword(
                account.id,
                to("password")
              )

            Await.result(updated, 30 seconds).get
          }
        }
      }
    else
      Future { Nil }

  def updateEmail(
    id:Long,
    email:String):Future[Option[Account]] =
    getById(id).map {
      case Some(account:Account) => {
        DB.withConnection { implicit connection =>
          val results = SQL(
            """
              UPDATE account a
              SET email = {email}
              WHERE id = {id};
            """
          ).on(
            'id -> id,
            'email -> email
          ).executeUpdate

          if (results > 0)
            Some(account)
          else
            None
        }
      }
      case _ => None
    }

  def updatePassword(id:Long,password:String):Future[Option[Account]] =
    getById(id).map {
      case Some(account:Account) => {
        DB.withConnection { implicit connection =>
          val results = SQL(
            """
              UPDATE account a
              SET password = {password}
              WHERE id = {id};
            """
          ).on(
            'id -> id,
            'password -> password
          ).executeUpdate

          if (results > 0)
            Some(account)
          else
            None
        }
      }
      case _ => None
    }

  def delete(id:Long):Future[Option[Account]] =
    getById(id) map {
      case Some(account:Account) => {
        DB.withConnection { implicit connection =>
          val results = SQL(
            """
              DELETE
              FROM account
              WHERE id = {id};
            """
          ).on(
            'id -> id
          ).executeUpdate

          if (results > 0)
            Some(account)
          else
            None
        }
      }
      case _ => None
    }

  val evolutions = {

    var list = List[(String,String)]()

    for (i <- 0 until 100) {
      val first = data.FirstName.random
      val last = data.LastName.random
      val email = first + "." + last + "@" + data.Domain.random

      list = list :+ (email,"password")
    }

    list
  }

  def authenticate(user:User with Credentials) = {

    val email = user.getEmail
    val password = user.password

    (user.getEmail,user.password) match {
      case (email:String,password:String) => {
        Await.result(getByEmail(email), 5 seconds) match {
          case Some(account:Account) => {
            account.password == useSalt(password,account.salt)
          } 
          case _ => false
        }
      }
      case _ => false
    }
  }
}
