package com.tomislav0.roomer.screens.rooms

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomislav0.roomer.models.Room
import com.tomislav0.roomer.models.Task
import com.tomislav0.roomer.viewModels.RoomViewModel

@Composable
fun RoomOverviewScreen(
    navController: NavController,
    scrollState: ScrollState,
    roomId: String?,
    roomViewModel: RoomViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current
    var room by remember {
        mutableStateOf(Room())
    }
    var tasks by remember {
        mutableStateOf<List<Task>>(listOf())
    }
    LaunchedEffect(Unit) {
        roomViewModel.getRoom(roomId!!).invokeOnCompletion {
            room = roomViewModel.room.value!!
            tasks = room.tasks.sortedBy { it.deadline.isEmpty() }.sortedBy { it.isDone }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),

        ) {
        Row() {
            Text(
                text = "${room.name}",
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp),
                textAlign = TextAlign.Start,
                fontSize = 30.sp
            )
            IconButton(
                onClick = { navController.navigate("task/add/${roomId}") },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
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
                        .shadow(90.dp, shape = RoundedCornerShape(15.dp)),
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

                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(modifier = Modifier.align(Alignment.End)) {
                            for (it in item.assignedTo!!.map { it.initials }) {
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(text = it, fontSize = 18.sp, textAlign = TextAlign.End)
                            }

                        }

                    }

                    Column(verticalArrangement = Arrangement.Bottom) {
                        Checkbox(
                            checked = item.isDone,
                            onCheckedChange = {
                                tasks = tasks.map {
                                    if (it == item) {
                                        it.copy(isDone = !it.isDone)
                                    } else it
                                }
                                room.tasks = tasks
                                roomViewModel.createRoom(room).invokeOnCompletion {
                                    Toast.makeText(mContext, "Room toggle", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            },
                            modifier = Modifier.padding(top = if (item.deadline.isNotEmpty()) 17.dp else 10.dp)
                        )

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


