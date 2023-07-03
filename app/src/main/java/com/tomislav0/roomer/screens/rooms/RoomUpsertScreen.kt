package com.tomislav0.roomer.screens.rooms

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tomislav0.roomer.models.User

@ExperimentalMaterial3Api
@Composable
fun RoomUpsertScreen(navController: NavController, scrollState: ScrollState, id: String?) {
    val context = LocalContext.current
    var users by remember {
        mutableStateOf<List<User>>(
            mutableListOf(
                User(
                    id = 1,
                    name = "Tomislav",
                    surname = "Kovacevic",
                    initials = "TK",
                    gender = "m"
                ),
                User(id = 2, name = "Marko", surname = "Kuralic", initials = "MK", gender = "m"),
                User(id = 3, name = "Luka", surname = "Mirkovic", initials = "LM", gender = "m"),
                User(id = 4, name = "Miroljub", surname = "Sabo", initials = "MS", gender = "m"),
                User(id = 5, name = "Rutko", surname = "Mrilc", initials = "RM", gender = "m"),
                User(id = 6, name = "Zvrk", surname = "Zuzo", initials = "ZZ", gender = "m")
            )
        )
    }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var members by remember { mutableStateOf(users) }
    var shownMembers by remember { mutableStateOf(users) }
    var selectedMembers by remember { mutableStateOf(mutableStateListOf<User>()) }
    var member by remember {
        mutableStateOf("")
    }
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create room", fontSize = 30.sp)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Name") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "Description") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Description") },
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
                label = { Text(text = "Add members") },
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

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(selectedMembers) { item ->
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "${item.name} ${item.surname}", textAlign = TextAlign.Justify,modifier = Modifier.align(Alignment.CenterVertically),)
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
            onClick = { /* Perform create room */ },
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

        ) {
            Text(text = "Create room")
        }

    }
}




