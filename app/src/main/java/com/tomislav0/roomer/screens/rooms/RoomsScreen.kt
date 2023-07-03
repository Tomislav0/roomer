package com.tomislav0.roomer.screens.rooms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tomislav0.roomer.models.Room

@Composable
fun RoomsScreen(navController: NavController, scrollState: ScrollState) {
    var rooms = remember { mutableStateOf<List<Room>>(getRooms()) }
    val lazyListState = rememberLazyListState()
    val firstItemTranslationY by remember {
        derivedStateOf {
            when {
                lazyListState.layoutInfo.visibleItemsInfo.isNotEmpty() &&
                        lazyListState.firstVisibleItemIndex == 0
                -> lazyListState.firstVisibleItemScrollOffset * .8f
                else -> 0f
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            state = lazyListState
        )
        {
            item() {
                SearchOutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 50.dp)
                        .graphicsLayer {
                            translationY = firstItemTranslationY
                        },
                    onSearchTextChanged = { })
                Spacer(modifier = Modifier.size(20.dp))
            }
            items(rooms.value) { item ->
                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xFF304430))
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 15.dp)
                        .clickable {
                            navController.navigate("room/${item.id}")
                        },
                ) {
                    Column() {
                        Text(text = item.name, fontSize = 30.sp)
                        Text(text = item.description, fontSize = 18.sp)
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = "1 task assigned to you", fontSize = 15.sp)
                    }
                    Column() {
                        Row() {
                            for (it in listOf("TK", "GV", "FC")) {
                                Text(text = it, fontSize = 18.sp)
                                Spacer(modifier = Modifier.size(10.dp))
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }

    }

}

@Composable
fun SearchOutlinedTextField(
    onSearchTextChanged: (String) -> Unit,
    modifier: Modifier
) {
    val searchQuery = remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        value = searchQuery.value,
        onValueChange = { newValue ->
            searchQuery.value = newValue
            onSearchTextChanged(newValue)
        },
        shape = RoundedCornerShape(30.dp),

        trailingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Enter room name, username..."
            )
        },
        label = {
            Text(text = "Search")
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray
        )
    )
}

fun getRooms(): List<Room> {
    return mutableListOf(
        Room(
            id = "1",
            name = "first",
            description = "firstDescription",
            members = null
        ),
        Room(
            id = "2",
            name = "second",
            description = "secondDescription",
            members = null
        ),
        Room(
            id = "3",
            name = "third",
            description = "thirdDescription",
            members = null
        ),
        Room(
            id = "4",
            name = "first",
            description = "firstDescription",
            members = null
        ),
        Room(
            id = "5",
            name = "second",
            description = "secondDescription",
            members = null
        ),
        Room(
            id = "6",
            name = "third",
            description = "thirdDescription",
            members = null
        )
    )
}