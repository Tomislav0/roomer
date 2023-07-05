package com.tomislav0.roomer.screens.auth

import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.viewModels.ForgotPasswordViewModel
import com.tomislav0.roomer.viewModels.RegisterViewModel

@ExperimentalMaterial3Api
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    scrollState: ScrollState,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Reset password", fontSize = 30.sp)
            Spacer(modifier = Modifier.size(50.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.size(20.dp))

            Button(
                onClick = {
                    viewModel.sendPasswordResetEmail(email).invokeOnCompletion {
                        if (viewModel.sendPasswordResetEmailResponse == Response.Success(true)) {
                            Toast.makeText(
                                context,
                                "Success. Please check email.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            navController.navigate("login")
                        } else {
                            Toast.makeText(context, "Wrong email.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .imePadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            ) {
                Text(text = "Reset password")
            }

        }
    }
}
