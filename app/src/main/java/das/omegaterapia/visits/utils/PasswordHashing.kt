package das.omegaterapia.visits.utils

import java.math.BigInteger
import java.security.MessageDigest

private val md = MessageDigest.getInstance("SHA-512")

fun String.hash(): String {
    val messageDigest = md.digest(this.toByteArray())
    val no = BigInteger(1, messageDigest)
    var hashText = no.toString(16)
    while (hashText.length < 32) {
        hashText = "0$hashText"
    }
    return hashText
}

fun String.compareHash(hashedString: String?): Boolean = this.hash() == hashedString
