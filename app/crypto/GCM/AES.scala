package crypto.gcm

import java.security._

import javax.crypto._
import javax.crypto.spec._

trait AES extends crypto.Scheme {

  val secretKey = {
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(128)
    keyGenerator.generateKey()
  }

  protected def initializationVector(bytes:Array[Byte]) = new IvParameterSpec(bytes)

  protected implicit val cipher:Cipher =
    Cipher.getInstance("AES/GCM/NoPadding","BC")

  protected def getIV(implicit cipher:Cipher) = {

    cipher
      .getParameters()
      .getParameterSpec(classOf[IvParameterSpec])
      .getIV()
  }

  protected def encrypt(input:Array[Byte],salt:Array[Byte])(implicit cipher:Cipher) = {
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, initializationVector(salt))
    cipher.doFinal(input)
  }

  protected def decrypt(input:Array[Byte])(implicit cipher:Cipher,iv:Array[Byte]) = {
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv))
    cipher.doFinal(input)
  }
}
