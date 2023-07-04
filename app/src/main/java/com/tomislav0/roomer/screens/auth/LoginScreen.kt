package com.tomislav0.roomer.screens.auth

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tomislav0.roomer.ContentActivity
import com.tomislav0.roomer.R
import com.tomislav0.roomer.dataAccess.StoreData
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.viewModels.LoginViewModel
import com.tomislav0.roomer.viewModels.RegisterViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(navController: NavController, scrollState: ScrollState, viewModel: LoginViewModel = hiltViewModel()) {
    val emailState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }
    val db = Firebase.firestore
    val auth = Firebase.auth
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

        val scope = rememberCoroutineScope()
        Button(
            onClick = {
                viewModel.signInWithEmailAndPassword(emailState.value.text, passwordState.value.text)
                    .invokeOnCompletion {
                            if(viewModel.signInResponse == Response.Success(true)){
                                Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show()
                                val dataStore = StoreData(mContext)

                                scope.launch {
                                    dataStore.saveData(emailState.value.text,passwordState.value.text)
                                }


                                val i = Intent(mContext, ContentActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                mContext.startActivity(i)
                            }else{
                                Toast.makeText(mContext, "Wrong email or password.", Toast.LENGTH_SHORT).show()
                            }
                        }
                      },
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
