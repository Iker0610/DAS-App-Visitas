package das.omegaterapia.visits.utils

import android.telephony.PhoneNumberUtils
import java.util.*
import java.util.regex.Pattern


fun isNumeric(number: String): Boolean = number.all { it.isDigit() }
fun isNonEmptyNumeric(number: String): Boolean = number.isNotBlank() && isNumeric(number)

fun isText(text: String) = text.all { it == ' ' || it.isLetter() }
fun isNonEmptyText(text: String) = text.isNotBlank() && isText(text)


fun isAlphaNumeric(text: String): Boolean = text.all { it.isLetterOrDigit() }
fun isNonEmptyAlphaNumeric(text: String): Boolean = text.isNotBlank() && isAlphaNumeric(text)

fun canBeZIP(zip: String): Boolean = zip.length <= 5 && isNumeric(zip)
fun isZIP(zip: String): Boolean = zip.length == 5 && isNumeric(zip)

fun canBePhoneNumber(phoneNumber: String): Boolean =
    phoneNumber.isEmpty() || ((phoneNumber[0].isDigit() || phoneNumber[0] == '+') && phoneNumber.drop(1).all { it.isDigit() || it == ' ' })

fun isValidPhoneNumber(phoneNumber: String, country: String = Locale.getDefault().country): Boolean =
    PhoneNumberUtils.formatNumberToE164(phoneNumber, country) != null

fun formatPhoneNumber(phoneNumber: String, country: String = Locale.getDefault().country): String =
    PhoneNumberUtils.formatNumberToE164(phoneNumber, country) ?: phoneNumber


val passwordMatcher = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$")
fun isValidPassword(password: String): Boolean = password.length in 5..30 && passwordMatcher.containsMatchIn(password)
fun canBePassword(password: String): Boolean = password.length <= 30 && password.all { it != ' ' }

fun canBeValidUsername(username: String): Boolean = username.length <= 20 && isAlphaNumeric(username)
fun isValidUsername(username: String): Boolean = username.isNotBlank() && canBeValidUsername(username)