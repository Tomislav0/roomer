package com.tomislav0.roomer.screens.rooms

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomislav0.roomer.models.Room
import com.tomislav0.roomer.models.User
import com.tomislav0.roomer.viewModels.RoomViewModel
import com.tomislav0.roomer.viewModels.UserViewModel
import kotlinx.coroutines.flow.first

@Composable
fun RoomsScreen(
    navController: NavController,
    viewModel: RoomViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    var currentUser by remember {
        mutableStateOf<User>(User())
    }
    var rooms by remember { mutableStateOf<List<Room>>(listOf()) }
    var shownRooms by remember { mutableStateOf<List<Room>>(listOf()) }

    LaunchedEffect(Unit) {
        currentUser = userViewModel.currentUser.first().first()
        viewModel.getRooms(currentUser).invokeOnCompletion {
            rooms = viewModel.rooms.value
            shownRooms = viewModel.rooms.value
        }

    }
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
                    onSearchTextChanged = { search ->
                        shownRooms = rooms.filter {
                            it.name.lowercase()
                                .contains(search.lowercase()) || it.description.lowercase()
                                .contains(search.lowercase())
                        }
                    })
                Spacer(modifier = Modifier.size(20.dp))
            }
            itemsIndexed(shownRooms) { index, item ->
                Column(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(
                            color = if (index % 2 == 0) Color(0x55539F52) else Color(
                                0x95354285
                            )
                        )
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("room/${item.id}")
                        }
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .shadow(90.dp, shape = RoundedCornerShape(15.dp)),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 5.dp)
                    ) {
                        Text(text = item.name, fontSize = 25.sp)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.align(Alignment.End)) {
                                for (it in item.members!!.map { it.initials }) {

                                    Spacer(modifier = Modifier.size(10.dp))
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.padding(top = 5.dp)
                                    )
                                }

                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        text = item.description,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 5.dp)
                    )

                    Spacer(modifier = Modifier.size(20.dp))
                    val assigned = item.tasks.filter {
                        it.assignedTo.contains(currentUser) && !it.isDone
                    }
                    Text(
                        text = if (assigned.size != 0) if (assigned.size == 1) "${assigned.size} task assigned to you" else "${assigned.size} tasks assigned to you" else "No tasks assigned to you",
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 5.dp)
                    )

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
        }
    )
}
