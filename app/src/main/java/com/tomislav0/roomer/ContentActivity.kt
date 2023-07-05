package com.tomislav0.roomer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.tomislav0.roomer.models.MenuItem
import com.tomislav0.roomer.screens.rooms.AddTaskScreen
import com.tomislav0.roomer.screens.rooms.ConnectionsScreen
import com.tomislav0.roomer.screens.rooms.RoomOverviewScreen
import com.tomislav0.roomer.screens.rooms.RoomUpsertScreen
import com.tomislav0.roomer.screens.rooms.RoomsScreen
import com.tomislav0.roomer.screens.rooms.TasksScreen
import com.tomislav0.roomer.ui.theme.RoomerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomerTheme {
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val imeState = rememberImeState()
                val scrollState = rememberScrollState()
                val context = LocalContext.current
                LaunchedEffect(key1 = imeState.value) {
                    if (imeState.value) {
                        scrollState.animateScrollTo(scrollState.value, tween(300))
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                    ModalNavigationDrawer(
                        scrimColor = Color.Transparent,
                        drawerState = drawerState,

                        drawerContent = {
                            ModalDrawerSheet(
                                modifier = Modifier
                                    .width(250.dp),
                            ) {
                                DrawerHeader()
                                DrawerBody(
                                    items = getMenuItems(),
                                    onItemClick = {
                                        when (it.id) {
                                            "logout" -> {
                                                FirebaseAuth.getInstance().signOut()
                                                context.startActivity(
                                                    Intent(
                                                        context,
                                                        MainActivity::class.java
                                                    )
                                                )
                                            }

                                            "rooms" -> {
                                                navController.navigate("rooms")
                                            }

                                            "tasks" -> {
                                                navController.navigate("tasks")
                                            }

                                            "connections" -> {
                                                navController.navigate("connections")
                                            }
                                        }
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    })
                            }

                        }
                    ) {
                        Scaffold(
                            topBar = {
                                AppBar(onNavigationIconClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }, navController = navController)
                            },
                            floatingActionButton = {

                                // Access the current route
                                val currentRoute: State<NavBackStackEntry?> =
                                    navController.currentBackStackEntryAsState()

                                if (currentRoute.value?.destination?.route == "rooms") {
                                    FloatingActionButton(
                                        modifier = Modifier.padding(20.dp),
                                        onClick = { navController.navigate("room/upsert/id") },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Add room"
                                            )
                                        }
                                    )
                                }
                            }
                        ) {

                            Box(modifier = Modifier.padding(it)) {

                                NavHost(navController, startDestination = "rooms") {
                                    composable("rooms") {
                                        RoomsScreen(navController)
                                    }
                                    composable(
                                        "room/{id}",
                                        arguments = listOf(navArgument("id") {
                                            type = NavType.StringType
                                        })
                                    ) { backStackEntry ->
                                        RoomOverviewScreen(
                                            navController,
                                            scrollState,
                                            backStackEntry.arguments?.getString("id")
                                        )
                                    }
                                    composable(
                                        "room/upsert/{id}",
                                        arguments = listOf(navArgument("id") {
                                            type = NavType.StringType
                                        })
                                    ) { backStackEntry ->
                                        RoomUpsertScreen(
                                            navController,
                                            scrollState,
                                            backStackEntry.arguments?.getString("id")
                                        )
                                    }
                                    composable(
                                        "task/add/{roomId}",
                                        arguments = listOf(navArgument("roomId") {
                                            type = NavType.StringType
                                        })
                                    ) { backStackEntry ->
                                        AddTaskScreen(
                                            navController,
                                            scrollState,
                                            backStackEntry.arguments?.getString("roomId")
                                        )
                                    }
                                    composable(
                                        "tasks"
                                    ) {
                                        TasksScreen(
                                            navController,
                                        )
                                    }
                                    composable(
                                        "connections"
                                    ) {
                                        ConnectionsScreen()
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun getMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = "rooms",
            title = "Rooms",
            contentDescription = "Go to rooms screen",
            icon = Icons.Default.Home
        ),
        MenuItem(
            id = "tasks",
            title = "Tasks",
            contentDescription = "Go to tasks screen",
            icon = Icons.Default.Check
        ),
        MenuItem(
            id = "connections",
            title = "Connections",
            contentDescription = "Go to connections screen",
            icon = Icons.Default.Person
        ),
        MenuItem(
            id = "logout",
            title = "Logout",
            contentDescription = "Go to Logout screen",
            icon = Icons.Default.ArrowBack
        )
    )
}