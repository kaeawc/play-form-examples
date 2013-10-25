package crypto

import java.security.SecureRandom

import javax.crypto._
import javax.crypto.spec._

trait DES extends Scheme {

  protected val secretKeySpec = new SecretKeySpec("asdfasdf".getBytes(), "DES");

  protected val initializationVector = {
    val random = new SecureRandom()
    val iv = new Array[Byte](16)
    random.nextBytes(iv)
    new IvParameterSpec(iv)
  }
  
  protected implicit def cipher:Cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")

  protected def encrypt(input:Array[Byte])(implicit cipher:Cipher) = {
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVector)

    val encrypted = new Array[Byte](cipher.getOutputSize(input.length))
    var enc_len = cipher.update(input, 0, input.length, encrypted, 0)
    cipher.doFinal(encrypted, enc_len)
    encrypted
  }

  protected def decrypt(input:Array[Byte])(implicit cipher:Cipher,iv:Array[Byte]) = {
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVector)

    val encrypted = new Array[Byte](cipher.getOutputSize(input.length))
    var enc_len = cipher.update(input, 0, input.length, encrypted, 0)
    cipher.doFinal(encrypted, enc_len)
    encrypted
  }
}
