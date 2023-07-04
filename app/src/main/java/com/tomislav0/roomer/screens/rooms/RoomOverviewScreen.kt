package com.tomislav0.roomer.screens.rooms

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RoomOverviewScreen(navController: NavController, scrollState: ScrollState, roomId: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.End

    ) {
        Button(onClick = { navController.navigate("task/add/${roomId}") }, modifier = Modifier.padding(top = 10.dp)) {
            Text(text = "Add Task")
        }
    }

}
