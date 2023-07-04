package com.tomislav0.roomer.screens.rooms

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomislav0.roomer.models.Room
import com.tomislav0.roomer.models.User
import com.tomislav0.roomer.viewModels.RoomViewModel
import com.tomislav0.roomer.viewModels.UserViewModel
import kotlinx.coroutines.flow.first
import java.util.UUID.randomUUID

@Composable
fun AddTaskScreen(navController: NavController, scrollState: ScrollState, roomId: String?, userViewModel:UserViewModel = hiltViewModel(), roomsViewModel: RoomViewModel= hiltViewModel() ) {
    var members by remember {
        mutableStateOf<List<User>>(listOf())
    }
    var shownMembers by remember {
        mutableStateOf(members)
    }
    var currentUser by remember {
        mutableStateOf<User>(User())
    }

    var room by remember {
        mutableStateOf(Room())
    }
    LaunchedEffect(Unit) {
        currentUser = userViewModel.currentUser.first().first()
        roomsViewModel.getRoom(roomId!!).invokeOnCompletion {
            room = roomsViewModel.room.value!!
            members = room.members!!
            shownMembers = room.members!!
        }
    }
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedMembers by remember { mutableStateOf(mutableStateListOf<User>()) }
    var member by remember {
        mutableStateOf("")
    }
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),

    ) {
        Text(
            text = "Add Task - ${room.name}",
            modifier = Modifier.padding(top=30.dp),
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Task Name") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Task Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Task Description") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Task Description") },
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
                value = member,
                label = { Text(text = "Assign To") },
                onValueChange = { value ->
                    member = value
                    if (value.length == 0) {
                        shownMembers = members.filter {
                            !selectedMembers.contains(
                                it
                            )
                        }
                    } else {
                        shownMembers = members.filter {
                            (it.name.lowercase()
                                .contains(value.lowercase()) || it.surname.lowercase()
                                .contains(value.lowercase())) && !selectedMembers.contains(
                                it
                            )
                        }
                    }
                    if (!expanded) {
                        expanded = true
                    }
                },
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
                if (shownMembers.isNotEmpty()) {
                    shownMembers.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = "${item.name} ${item.surname}") },
                            onClick = {
                                selectedMembers.add(item)
                                member = ""
                                shownMembers = members.filter {
                                    !selectedMembers.contains(
                                        it
                                    )
                                }
                                expanded = false
                            }
                        )
                    }
                } else {
                    DropdownMenuItem(
                        text = { Text(text = "Users not found", textAlign = TextAlign.Center) },
                        onClick = {
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(20.dp))

        LazyColumn(
            modifier = Modifier
                .padding(20.dp)
                .weight(1f)
        ) {
            items(selectedMembers) { item ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${item.name} ${item.surname}",
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                    Button(onClick = {
                        selectedMembers.remove(item)
                        shownMembers = members.filter {
                            !selectedMembers.contains(
                                it
                            )
                        }
                    }) {
                        Text(text = "Remove")
                    }
                }

            }
        }

        Button(
            onClick = {
                selectedMembers.add(currentUser)
                roomsViewModel.createRoom(
                    Room(
                        randomUUID().toString(),
                        name,
                        description,
                        selectedMembers
                    )
                ).invokeOnCompletion {
                    Toast.makeText(context, "Successful created", Toast.LENGTH_SHORT).show()
                    navController.navigate("rooms")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)

        ) {
            Text(text = "Create room")
        }


    }

}
