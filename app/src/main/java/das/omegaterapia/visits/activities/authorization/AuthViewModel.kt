package das.omegaterapia.visits.activities.authorization

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.model.entities.AuthUser
import das.omegaterapia.visits.model.repositories.ILoginRepository
import das.omegaterapia.visits.model.repositories.LoginRepository
import das.omegaterapia.visits.preferences.ILoginSettings
import das.omegaterapia.visits.utils.compareHash
import das.omegaterapia.visits.utils.hash
import das.omegaterapia.visits.utils.isValidPassword
import das.omegaterapia.visits.utils.isValidUsername
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val loginRepository: ILoginRepository) : ViewModel() {
    val lastLoggedUser: String?
        get() = runBlocking { return@runBlocking loginRepository.getLastLoggedUser() }
            .let {
                if (it != null && runBlocking { return@runBlocking loginRepository.getUserPassword(it) } != null) {
                    it
                } else null
            }

    // Screen States
    var isLogin: Boolean by mutableStateOf(true)
        private set


    // Login States
    var isLoginCorrect by mutableStateOf(true)
        private set

    var loginUsername by mutableStateOf(lastLoggedUser ?: "")
    var loginPassword by mutableStateOf("")


    // Sign In States
    var signInUsername by mutableStateOf("")
    var signInPassword by mutableStateOf("")
    var signInConfirmationPassword by mutableStateOf("")

    val isSignInUsernameValid by derivedStateOf { isValidUsername(signInUsername) }
    val isSignInPasswordValid by derivedStateOf { isValidPassword(signInPassword) }
    val isSignInPasswordConfirmationValid by derivedStateOf { isSignInPasswordValid && signInPassword == signInConfirmationPassword }

    var signInUserExists by mutableStateOf(false)


    //-------------------------------------------------------------------
    // Events
    //-------------------------------------------------------------------
    fun switchScreen() {
        isLogin = !isLogin
    }


    fun updateLastLoggedUsername(username: String) = runBlocking {
        loginRepository.setLastLoggedUser(username)
    }


    //-------------------------------------------------------------------
    // Login Events
    suspend fun checkLogin(): String? {
        val username = loginUsername
        isLoginCorrect = loginPassword.compareHash(loginRepository.getUserPassword(username))
        return if (isLoginCorrect) username else null
    }


    //-------------------------------------------------------------------
    // Sign-In Events
    suspend fun checkSignIn(): String? {
        if (isSignInUsernameValid && isSignInPasswordConfirmationValid) {
            val newUser = AuthUser(signInUsername, signInPassword.hash())
            val signInCorrect = loginRepository.createUser(newUser)
            signInUserExists = !signInCorrect
            return if (signInCorrect) newUser.username else null
        }
        return null
    }
}