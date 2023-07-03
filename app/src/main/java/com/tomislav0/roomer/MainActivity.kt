package com.tomislav0.roomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomislav0.roomer.screens.auth.ForgotPasswordScreen
import com.tomislav0.roomer.screens.auth.LoginScreen
import com.tomislav0.roomer.screens.auth.RegistrationScreen
import com.tomislav0.roomer.ui.theme.RoomerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomerTheme {
                val navController = rememberNavController()
                val imeState = rememberImeState()
                val scrollState = rememberScrollState()

                LaunchedEffect(key1 = imeState.value) {
                    if (imeState.value) {
                        scrollState.animateScrollTo(scrollState.value, tween(300))
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {

                    NavHost(navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(navController, scrollState)
                        }
                        composable("registration") {
                            RegistrationScreen(navController, scrollState)
                        }

                        composable("forgot_password") {
                            ForgotPasswordScreen(navController, scrollState)
                        }

                    }
                }













            }
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun HamburgerBar(
    modifier: Modifier = Modifier,
    onMenuItemClicked: (String) -> Unit
) {
    val isMenuOpen = remember { mutableStateOf(false) }

    Box {
        IconButton(
            modifier = modifier,
            onClick = { isMenuOpen.value = true }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
            )
        }

        if (isMenuOpen.value) {
            DropdownMenu(
                modifier = Modifier.wrapContentWidth(align = Alignment.Start),
                expanded = isMenuOpen.value,
                onDismissRequest = { isMenuOpen.value = false }
            ) {
                Column {
                    DropdownMenuItem(text = { Text("Rooms") },
                        onClick = {
                            isMenuOpen.value = false
                            onMenuItemClicked("Rooms")
                        })
                    DropdownMenuItem(text = { Text("Tasks") },
                        onClick = {
                            isMenuOpen.value = false
                            onMenuItemClicked("Tasks")
                        })
                    DropdownMenuItem(text = { Text("Connections") },
                        onClick = {
                            isMenuOpen.value = false
                            onMenuItemClicked("Connections")
                        })
                    DropdownMenuItem(text = { Text("Settings") },
                        onClick = {
                            isMenuOpen.value = false
                            onMenuItemClicked("Settings")
                        })
                }
            }
        }
    }
}