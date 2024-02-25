package com.example.duno




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.duno.ui.Colors
import timber.log.Timber

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DunoNavGraph(
    navController: NavHostController,
    startDestination: String = Events.route
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ){
        composable(DunoScreens.EVENTS_SCREEN){
            EventsScreen()
        }
        composable(DunoScreens.MAP_SCREEN){
            MapScreen()
        }
        composable(DunoScreens.PROFILE_SCREEN){
            ProfileScreen()
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: Events.route

    Scaffold (
        topBar = {if (selectedDestination != DunoScreens.MAP_SCREEN) {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(selectedDestination)
                }
            )
        }
            else {
                Text(selectedDestination)}
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Colors.md_Surface,
            ) {
                CommonUI(
                    navController = navController,
                    selectedDestination = selectedDestination,
                )
            }
        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {}) {
//                Icon(Icons.Filled.Add, contentDescription = "Add")
//            }
//        }
        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DunoNavGraph(
                navController,
            )
        }
    }
}

@Composable
fun CommonUI(
    //navigate: () ->  Unit
    navController: NavHostController,
    selectedDestination: String,
) {
    val selectedItem by remember { mutableStateOf(DunoRowScreens) }
    Box(modifier = Modifier.fillMaxSize()) {
        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            containerColor = Colors.md_Surface,
        ) {
            selectedItem.forEachIndexed { index,  item ->
                NavigationBarItem(
                    icon = {Icon(
                        if (selectedDestination == item.route)
                            item.selectedIcon
                        else item.unselectedIcon,
                        contentDescription = null) },
                    label = { Text(item.text) },
                    selected = selectedDestination == item.route,
                    onClick = {
                        navController.navigateSingleTopTo(item.route)
                        Timber.e(selectedDestination)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Colors.md_PrimaryContainer,
                        indicatorColor = Colors.md_Surface
                    )
                )
            }
        }
    }
}




fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        //restoreState = true
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Box(modifier = Modifier.padding(top = 12.dp, start = 12.dp)) {
            var selected by remember { mutableStateOf(false) }
            var text by rememberSaveable { mutableStateOf("") }
            TextField(
                modifier = Modifier.size(140.dp,40.dp),
                value = text,
                onValueChange = { text = it },
                label = { Text("Label") },
                singleLine = true
            )
            FilterChip(
                modifier = Modifier.padding( end = 0.dp),
                onClick = {
                    selected = !selected
                    Timber.d("DnD filter")
                          },
                label = { Text("DnD") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Localized description",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                selected = selected,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Colors.md_Primary,
                    iconColor = Colors.md_Background,
                )
            )
        }
    }
}

@Composable
fun MapScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Text(text = "uy")
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Text(text = "uty")
    }
}


