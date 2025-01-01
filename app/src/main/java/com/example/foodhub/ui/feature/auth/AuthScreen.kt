package com.example.foodiehub.ui.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
import com.example.foodhub.R
import com.example.foodhub.ui.GroupSocialButtons
import com.example.foodhub.ui.navigation.Login
import com.example.foodhub.ui.navigation.SignUp

import com.example.foodhub.ui.theme.orange

@Composable
fun AuthScreen(
    navController: NavController
){
    val imageSize = remember { mutableStateOf(IntSize.Zero) }

    val brush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
        startY = imageSize.value.height.toFloat() / 3,
    )
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)){
        Image(
            painter = painterResource(R.drawable.background), contentDescription = null,
            modifier = Modifier
                .onGloballyPositioned {
                    imageSize.value = it.size
                }
                .alpha(0.6f),
            contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )

        Button(
            onClick = {

            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
        )
        {
            Text(text = stringResource(R.string.skip), color = orange)
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 110.dp).padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome),
                color = Color.Black,
                modifier = Modifier,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.app_name),
                color = orange,
                modifier = Modifier,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.app_description),
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GroupSocialButtons(onFacebookClick = { /*TODO*/ }, onGoogleClick = { /*TODO*/ })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {navController.navigate(SignUp) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Text(text = stringResource(id = R.string.sign_with_email), color = Color.White)
            }

            TextButton(onClick = { navController.navigate(Login)}) {
                Text(text = stringResource(id = R.string.already_have_an_account), color = Color.White)
            }
        }
    }
}

//@Composable
//fun AuthScreen1(){
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.Black)){
//        Image(
//            painter = painterResource(R.drawable.background), contentDescription = null,modifier = Modifier.alpha(0.6f)
//        )
//    }
//}


@Preview
@Composable
fun AuthScreenPreview(){
    AuthScreen(rememberNavController())
}

//@Preview
//@Composable
//fun AuthScreenPreview1(){
//    AuthScreen1()
//
//}