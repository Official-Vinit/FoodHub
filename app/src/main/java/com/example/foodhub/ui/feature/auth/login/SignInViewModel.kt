package com.example.foodhub.ui.feature.auth.login

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodhub.data.FoodApi
import com.example.foodhub.data.auth.GoogleAuthUiProvider
import com.example.foodhub.data.models.OAuthRequest
import com.example.foodhub.data.models.SignInRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(val foodApi: FoodApi) : ViewModel() {
    private val _uiState = MutableStateFlow<SignInEvent>(SignInEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SigninNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    val googleAuthUiProvider = GoogleAuthUiProvider()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _eyeClick = MutableStateFlow(false)
    val eyeClick = _eyeClick.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onEyeButtonClicked(){
        _eyeClick.value = _eyeClick.value
    }

    fun onSignInClick() {

        viewModelScope.launch{
            _uiState.value = SignInEvent.Loading
            try {
                val response = foodApi.SignIn(
                    SignInRequest(
                        email = _email.value,
                        password = _password.value
                    )
                )
                if(response.token.isNotEmpty()){
                    _uiState.value = SignInEvent.Success
                    _navigationEvent.emit(SigninNavigationEvent.NavigateToHome)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SignInEvent.Error
            }
        }
    }

    fun onGoogleSignInClicked(context: Context){
        viewModelScope.launch{
            _uiState.value = SignInEvent.Loading
            val response = googleAuthUiProvider.signIn(
                context,
                CredentialManager.create(context)
            )

            if (response!=null){
                val request = OAuthRequest(
                    token = response.token,
                    provider = "google"
                )

                val res = foodApi.oAuth(request)

                if (res.token.isNotEmpty()){
                    Log.d("SignInViewModel", "onGoogleSignInClicked: $(res.token)")
                    _uiState.value = SignInEvent.Success
                    _navigationEvent.emit(SigninNavigationEvent.NavigateToHome)
                }else{
                    _uiState.value = SignInEvent.Error
                }
            } else {
                _uiState.value = SignInEvent.Error
            }
        }
    }

    fun onSignUpClicked(){
        viewModelScope.launch {
            _navigationEvent.emit(SigninNavigationEvent.NavigateToSignUp)
        }
    }

    sealed class SigninNavigationEvent {
        object NavigateToSignUp : SigninNavigationEvent()
        object NavigateToHome : SigninNavigationEvent()
    }

    sealed class SignInEvent {
        object Nothing : SignInEvent()
        object Success : SignInEvent()
        object Error : SignInEvent()
        object Loading : SignInEvent()
    }
}