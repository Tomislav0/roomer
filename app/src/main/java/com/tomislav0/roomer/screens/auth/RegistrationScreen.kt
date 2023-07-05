package com.tomislav0.roomer.screens.auth

import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ch.benlu.composeform.validators.EmailValidator
import com.google.firebase.auth.FirebaseAuth
import com.tomislav0.roomer.ContentActivity
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.models.User
import com.tomislav0.roomer.viewModels.LoginViewModel
import com.tomislav0.roomer.viewModels.RegisterViewModel
import com.tomislav0.roomer.viewModels.UserViewModel

@ExperimentalMaterial3Api
@Composable
fun RegistrationScreen(
    navController: NavController,
    scrollState: ScrollState,
    viewModel: RegisterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val genders = arrayOf("Male", "Female", "Prefer not to say")

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
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
            Text(text = "Registration", fontSize = 30.sp)
            Spacer(modifier = Modifier.size(50.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text(text = "Repeat password") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Divider(
                thickness = 1.dp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Name") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text(text = "Surname") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Surname") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = gender,
                    label = { Text(text = "Gender") },
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Gender") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genders.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                gender = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.size(20.dp))

            Button(
                onClick = {
                    if (email == "" || password == "" || repeatPassword == "" || name == "" || surname == "" || gender == "") {
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    } else if (!EmailValidator().validate(email)) {
                        Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show()
                        return@Button
                    } else if (password.length < 6) {
                        Toast.makeText(
                            context,
                            "Password must contain at least 6 characters.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    } else if (password != repeatPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.signUpWithEmailAndPassword(email, password).invokeOnCompletion {
                        if ((viewModel.signUpResponse as Response.Success).data.length == 28) {
                            userViewModel.addUser(
                                User(
                                    (viewModel.signUpResponse as Response.Success).data,
                                    email,
                                    name,
                                    surname,
                                    "${name.first()}${surname.first()}",
                                    gender
                                )
                            )
                            Toast.makeText(context, "Success. Please login.", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate("login")
                        } else {
                            Toast.makeText(context, "Wrong email or password.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                },
                modifier = Modifier
                    .imePadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

            ) {
                Text(text = "Register")
            }

        }
    }
}
