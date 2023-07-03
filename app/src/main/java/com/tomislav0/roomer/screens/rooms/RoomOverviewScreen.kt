package com.tomislav0.roomer.screens.rooms

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun RoomOverviewScreen(navController: NavController, scrollState: ScrollState, roomId: String?) {
   roomId?.let { Text(it) }
}
