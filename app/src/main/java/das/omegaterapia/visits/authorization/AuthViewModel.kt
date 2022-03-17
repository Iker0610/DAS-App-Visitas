package das.omegaterapia.visits.authorization

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import das.omegaterapia.visits.model.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepository,
) : ViewModel() {

    // Screen States
    var isLogin: Boolean by mutableStateOf(true)
        private set


    // Login States
    var isLoginCorrect by mutableStateOf(true)
    var loginUsername by mutableStateOf("")
    var loginPassword by mutableStateOf("")


    // Sign In States
    var signInUsername by mutableStateOf("")
    var signInPassword by mutableStateOf("")
    var signInConfirmationPassword by mutableStateOf("")

    //-------------------------------------------------------------------
    // Events
    //-------------------------------------------------------------------

    fun switchScreen() {
        isLogin = !isLogin
    }

    //-------------------------------------------------------------------
    // Login Events

    fun submitLogin() {
        // TODO
    }

    fun submitBiometricLogin() {
        //  TODO
    }

    //-------------------------------------------------------------------
    // Sign-In Events
    fun submitSignIn() {
        // TODO
    }
}