package com.tomislav0.roomer.screens.rooms

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomislav0.roomer.models.Room
import com.tomislav0.roomer.models.Task
import com.tomislav0.roomer.models.User
import com.tomislav0.roomer.viewModels.RoomViewModel
import com.tomislav0.roomer.viewModels.UserViewModel
import kotlinx.coroutines.flow.first

@Composable
fun ConnectionsScreen(
    userViewModel: UserViewModel = hiltViewModel()
) {
    var currentUser by remember {
        mutableStateOf(User())
    }
    var users by remember {
        mutableStateOf<List<User>>(listOf())
    }

    LaunchedEffect(Unit) {
        users = userViewModel.users.first()
        currentUser = userViewModel.currentUser.first().first()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),

        ) {
        Row() {
            Text(
                text = "Connections",
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp),
                textAlign = TextAlign.Start,
                fontSize = 30.sp
            )
        }

        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn() {
            itemsIndexed(users) { index, item ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(
                            color = Color(0x55519FFF)
                        )
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .shadow(
                            230.dp, shape = RoundedCornerShape(15.dp), ambientColor = Color.Blue
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${item.name} ${item.surname}", fontSize = 15.sp)
                    Text(text = item.email, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
    }
}