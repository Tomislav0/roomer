package com.tomislav0.roomer.screens.rooms

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun TasksScreen(
    navController: NavController,
    roomViewModel: RoomViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    var currentUser by remember {
        mutableStateOf(User())
    }
    var tasks by remember {
        mutableStateOf<List<Task>>(listOf())
    }
    var rooms by remember { mutableStateOf<List<Room>>(listOf()) }

    LaunchedEffect(Unit) {
        currentUser = userViewModel.currentUser.first().first()
        roomViewModel.getRooms(currentUser).invokeOnCompletion {
            rooms = roomViewModel.rooms.value
            tasks = rooms
                .flatMap { room -> room.tasks }
                .filter { task -> currentUser in task.assignedTo && !task.isDone }
                .mapNotNull { task -> task.takeIf { it.assignedTo.contains(currentUser) && !it.isDone } }
                .sortedBy { it.deadline.isEmpty() }
            Log.v("Debug", tasks.size.toString())
        }

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),

        ) {
        Row() {
            Text(
                text = "My Active Tasks",
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp),
                textAlign = TextAlign.Start,
                fontSize = 30.sp
            )
        }

        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn() {
            itemsIndexed(tasks) { index, item ->
                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(
                            color = if (item.isDone) Color(0xF20000000) else if (index % 2 == 0) Color(
                                0x55539F52
                            ) else Color(
                                0x95354285
                            )
                        )
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .shadow(90.dp, shape = RoundedCornerShape(15.dp))
                        .clickable { navController.navigate("room/${item.roomId}") },
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(5.dp)
                    ) {
                        Text(text = item.name, fontSize = 20.sp)
                        Text(text = item.description, fontSize = 12.sp)
                        Spacer(modifier = Modifier.size(10.dp))
                        if (item.deadline.isNotEmpty()) {
                            Row() {
                                Text(
                                    text = "Deadline: ${item.deadline}",
                                    fontSize = 12.sp,
                                    color = Color(0xCFFFFF00)
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(top = 8.dp, end = 5.dp)) {
                        Row(modifier = Modifier.align(Alignment.End)) {
                            for (it in item.assignedTo!!.map { it.initials }) {
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(text = it, fontSize = 18.sp, textAlign = TextAlign.End)
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
        if (tasks.isEmpty()) {
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = "This room has no active tasks.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}