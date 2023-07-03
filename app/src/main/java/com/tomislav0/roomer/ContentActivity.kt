package com.tomislav0.roomer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.imageLoader
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.tomislav0.roomer.models.MenuItem
import com.tomislav0.roomer.screens.rooms.RoomOverviewScreen
import com.tomislav0.roomer.screens.rooms.RoomUpsertScreen
import com.tomislav0.roomer.screens.rooms.RoomsScreen
import com.tomislav0.roomer.ui.theme.RoomerTheme
import kotlinx.coroutines.launch

class ContentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomerTheme {
                val navController = rememberNavController()
                val imeState = rememberImeState()
                val scrollState = rememberScrollState()

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
                                        println("Clicked on ${it.title}")
                                    })
                            }

                        }
                    ) {
                        val scope = rememberCoroutineScope()
                        Scaffold(
                            topBar = {
                                AppBar(onNavigationIconClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                })
                            },
                            floatingActionButton = {

                                // Access the current route
                                val currentRoute: State<NavBackStackEntry?> = navController.currentBackStackEntryAsState()

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
                                        RoomsScreen(navController, scrollState)
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
            icon = Icons.Default.Home
        ),
        MenuItem(
            id = "connections",
            title = "Connections",
            contentDescription = "Go to connections screen",
            icon = Icons.Default.Home
        ),
        MenuItem(
            id = "settings",
            title = "Settings",
            contentDescription = "Go to settings screen",
            icon = Icons.Default.Home
        )
    )
}