package com.example.foodhub.ui.feature.auth.signup

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodhub.R
import com.example.foodhub.ui.FoodHubTextField
import com.example.foodhub.ui.GroupSocialButtons
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodhub.ui.theme.orange
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodhub.ui.navigation.AuthScreen
import com.example.foodhub.ui.navigation.Home
import com.example.foodhub.ui.navigation.Login
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = hiltViewModel()) {

    val name = viewModel.name.collectAsStateWithLifecycle()
    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val eyeClick = viewModel.eyeClick.collectAsStateWithLifecycle()
    val errorMessage = remember{ mutableStateOf<String?>(null) }
    val loading = remember{ mutableStateOf(false) }

    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {

        is SignUpViewModel.SignupEvent.Error -> {
            // show error
            loading.value = false
            errorMessage.value = "Failed"
        }

        is SignUpViewModel.SignupEvent.Loading -> {
            loading.value = true
            errorMessage.value = null
        }

        else -> {
            loading.value = false
            errorMessage.value = null
        }
    }


    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is SignUpViewModel.SignupNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home){
                        popUpTo(AuthScreen){
                            inclusive = true
                        }
                    }
                }
                is SignUpViewModel.SignupNavigationEvent.NavigateToLogin->{
                    navController.navigate(Login)
                }
                else->{

                }
            }
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Image(
            painter = painterResource(R.drawable.auth_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier=Modifier.weight(1f))
            Text(
                text = stringResource(R.string.sign_up),
                fontSize = 32.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.padding(20.dp))
            FoodHubTextField(
                value = name.value,
                onValueChange = { viewModel.onNameChange(it) },
                label = {
                    Text(
                        text = stringResource(R.string.full_name),
                        color = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            FoodHubTextField(
                value = email.value,
                onValueChange = {viewModel.onEmailChange(it)},
                label = {
                    Text(
                        text = stringResource(R.string.email),
                        color = Color.Gray
                    )                },
                modifier = Modifier.fillMaxWidth(),
            )
            FoodHubTextField(
                value = password.value,
                onValueChange = {viewModel.onPasswordChange(it)},
                label = {
                    Text(
                        text = stringResource(R.string.password),
                        color = Color.Gray
                    )                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (!eyeClick.value) {PasswordVisualTransformation()} else { VisualTransformation.None},
                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.eye),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                viewModel.onEyeButtonClicked()
                            }
                    )

                }
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = errorMessage.value?:"", color = Color.Red)
            Button(
                onClick = {
                    viewModel.onSignUpClick()
                },
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = orange)
            ) {
                Box() {
                    AnimatedContent(
                        targetState = loading.value,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(durationMillis = 500)) + scaleIn(
                                initialScale = 0.8f
                            ) togetherWith
                                    fadeOut(animationSpec = tween(durationMillis = 500)) + scaleOut(
                                targetScale = 0.8f
                            )

                        }
                    ) { loading ->
                        if (loading) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(24.dp).padding(32.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.sign_up),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource( R.string.already_have_an_account),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable {
                        viewModel.onLoginClicked()
                    }
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            GroupSocialButtons(color = Color.Black, onFacebookClick = { /*TODO*/ }, onGoogleClick = { /*TODO*/ })
        }
    }
}

@Preview
@Composable
fun SignUpPreview(){
    SignUpScreen(rememberNavController())
}

