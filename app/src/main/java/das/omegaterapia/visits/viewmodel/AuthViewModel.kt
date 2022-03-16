package das.omegaterapia.visits.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

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

    fun submitLogin(){
        // TODO
    }

    fun submitBiometricLogin(){
        //  TODO
    }

    //-------------------------------------------------------------------
    // Sign-In Events
    fun submitSignIn(){
        // TODO
    }
}