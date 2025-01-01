package com.example.foodhub.ui.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodhub.data.FoodApi
import com.example.foodhub.data.models.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(val foodApi: FoodApi) : ViewModel() {
    private val _uiState = MutableStateFlow<SignupEvent>(SignupEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignupNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _eyeClick = MutableStateFlow(false)
    val eyeClick = _eyeClick.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onEyeButtonClicked(){
        _eyeClick.value = !_eyeClick.value
    }

    fun onSignUpClick() {

        viewModelScope.launch{
            _uiState.value = SignupEvent.Loading
            try {
                val response = foodApi.signUp(
                    SignUpRequest(
                        name = _name.value,
                        email = _email.value,
                        password = _password.value
                    )
                )
                if(response.token.isNotEmpty()){
                    _uiState.value = SignupEvent.Success
                    _navigationEvent.emit(SignupNavigationEvent.NavigateToHome)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SignupEvent.Error
            }
        }
    }

    fun onLoginClicked(){
        viewModelScope.launch {
            _navigationEvent.emit(SignupNavigationEvent.NavigateToLogin)
        }
    }

    sealed class SignupNavigationEvent {
        object NavigateToLogin : SignupNavigationEvent()
        object NavigateToHome : SignupNavigationEvent()
    }

    sealed class SignupEvent {
        object Nothing : SignupEvent()
        object Success : SignupEvent()
        object Error : SignupEvent()
        object Loading : SignupEvent()
    }
}