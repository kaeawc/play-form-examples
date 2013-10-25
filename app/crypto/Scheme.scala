package crypto

import java.security._

import javax.crypto._
import javax.crypto.spec._
import org.bouncycastle.jce.provider._

trait Scheme {

  Security.addProvider(new BouncyCastleProvider())

  protected def randomBytes(length:Int):Array[Byte] = {
    val random = new SecureRandom()
    val bytes = new Array[Byte](length)
    random.nextBytes(bytes)
    bytes
  }

  // protected implicit def cipher:Cipher
  // protected def encrypt(input:Array[Byte])(implicit cipher:Cipher):Array[Byte]
  // protected def decrypt(input:Array[Byte])(implicit cipher:Cipher,iv:Array[Byte]):Array[Byte]
}
