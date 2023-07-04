package com.tomislav0.roomer

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    navController:NavController
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 45.dp)
            ) {
                val currentRoute: State<NavBackStackEntry?> = navController.currentBackStackEntryAsState()
                Log.v("Debug", currentRoute.value?.destination?.route.toString())

                when (currentRoute.value?.destination?.route) {
                    "room/upsert/{id}" -> Text(text = "Create room",  modifier = Modifier
                        .align(Alignment.Center))
                    else ->
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Logo",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.Center)
                        )
                }

            }


        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Toggle drawer")

            }
        }
    )
}