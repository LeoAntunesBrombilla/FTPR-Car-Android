package presentation.ui.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapitest.data.repository.AuthRepository
import com.example.myapitest.domain.model.AuthState
import com.google.firebase.auth.PhoneAuthProvider

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _authState = MutableLiveData<AuthState>()

    val authState: LiveData<AuthState> = _authState

    private val _verificationId = MutableLiveData<String>()

    fun sendPhoneVerification(phoneNumber: String, activity: FragmentActivity) {
        _authState.value = AuthState.Loading

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                // Auto-verification
                signInWithPhoneCredential(credential)
            }

            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                _authState.value = AuthState.Error(e.message ?: "Verification failed")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _verificationId.value = verificationId
                _authState.value = AuthState.CodeSent
            }
        }

        repository.sendVerificationCode(phoneNumber, activity, callbacks)
    }

    fun verifyCode(code: String) {
        _authState.value = AuthState.Loading
        val verificationId = _verificationId.value

        if (verificationId != null) {
            repository.verifyPhoneNumber(verificationId, code) { success, error ->
                if (success) {
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error(error ?: "Nao conseguimos verificar o código")
                }
            }
        } else {
            _authState.value = AuthState.Error("Identificador de verificação nulo")
        }
    }

    fun signInWithPhoneCredential(credential: com.google.firebase.auth.PhoneAuthCredential) {
        repository.signInWithCredential(credential) { success, error ->
            if (success) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(error ?: "Sign-in falhou")
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState.SignedOut
    }

    fun checkCurrentUser() {
        val user = repository.getCurrentUser()
        if (user != null) {
            _authState.value = AuthState.Success
        } else {
            _authState.value = AuthState.SignedOut
        }
    }
}