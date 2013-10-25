package models

import java.math.BigInteger
import java.security.SecureRandom

trait Authentication extends crypto.gcm.AES {

  def authenticate(user:User with Credentials):Boolean

  def createSalt = randomBytes(16)

  def useSalt(plainText:String, salt:Array[Byte]):String = {
    val cipherText = encrypt(plainText.getBytes("UTF8"), salt)
    new sun.misc.BASE64Encoder().encode(cipherText)
  }

  implicit def stringToBytes(str:String):Array[Byte] =
    str.toCharArray().map(_.toByte)

  implicit def bytesToString(bytes:Array[Byte]):String =
    new String(bytes.map(_.toChar))
}
