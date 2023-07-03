package com.tomislav0.roomer.screens.auth

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tomislav0.roomer.ContentActivity
import com.tomislav0.roomer.R

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(navController: NavController, scrollState: ScrollState) {
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val mContext = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.logo), // Replace with your logo image resource
            contentDescription = "Logo",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.size(50.dp))

        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text(text = "Email") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text(text = "Password") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Password"
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        TextButton(
            onClick = { navController.navigate("forgot_password") },
            modifier = Modifier
                .imePadding()
                .align(Alignment.Start)
        ) {
            Text(
                text = "Forgot password?",
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        Button(
            onClick = { mContext.startActivity(Intent(mContext, ContentActivity::class.java)) },
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

        ) {
            Text(text = "Login")
        }

        Divider(
            thickness = 1.dp,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
        TextButton(
            onClick = { navController.navigate("registration") }
        ) {
            Text(text = "Register", fontSize = 16.sp)
        }
    }


}
