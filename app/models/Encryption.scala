package models

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import scala.util.Random
import play.api.libs.Crypto

trait Encryption {

  protected def generate(length:Int):String = {
    val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9') ++ ("-!£$").mkString("")
    (1 to length).map(
      x => {
        val index = Random.nextInt(chars.length)
        chars(index)
      }
    ).mkString("")
  }

  protected def makeCipher:Cipher = {
    val key:String = "WxP4tS7C";
    val keyData:Array[Byte] = key.getBytes();
    val ks:SecretKeySpec = new SecretKeySpec(keyData, "Blowfish")
    val cipher:Cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, ks)
    cipher
  }

  protected def encrypt(plaintext:String) =
    Crypto.encryptAES(plaintext)

  protected def decrypt(ciphertext:String) =
    Crypto.decryptAES(ciphertext)

}
