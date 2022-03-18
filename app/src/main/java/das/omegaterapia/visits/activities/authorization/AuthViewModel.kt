package das.omegaterapia.visits.activities.authorization

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.model.entities.AuthUser
import das.omegaterapia.visits.model.repositories.UserRepository
import das.omegaterapia.visits.utils.compareHash
import das.omegaterapia.visits.utils.hash
import das.omegaterapia.visits.utils.isValidPassword
import das.omegaterapia.visits.utils.isValidUsername
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    // Screen States
    var isLogin: Boolean by mutableStateOf(true)
        private set


    // Login States
    var isLoginCorrect by mutableStateOf(true)
        private set

    var loginUsername by mutableStateOf("")
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


    //-------------------------------------------------------------------
    // Login Events
    suspend fun checkLogin(): String? {
        val username = loginUsername
        isLoginCorrect = loginPassword.compareHash(repository.getUserPassword(username))
        return if (isLoginCorrect) username else null
    }


    //-------------------------------------------------------------------
    // Sign-In Events
    suspend fun checkSignIn(): String? {
        if (isSignInUsernameValid && isSignInPasswordConfirmationValid) {
            val newUser = AuthUser(signInUsername, signInPassword.hash())
            val signInCorrect = repository.createUser(newUser)
            signInUserExists = !signInCorrect
            return if (signInCorrect) newUser.username else null
        }
        return null
    }
}