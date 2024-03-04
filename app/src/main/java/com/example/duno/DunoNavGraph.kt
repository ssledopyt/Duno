package com.example.duno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.duno.compose.EventsScreen
import com.example.duno.compose.MapScreen
import com.example.duno.compose.ProfileScreen
import com.example.duno.ui.Colors
import timber.log.Timber

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DunoNavGraph(
    navController: NavHostController,
    startDestination: String = Map.route
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
        topBar = {if (selectedDestination == DunoScreens.MAP_SCREEN) {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Duno")
                }
            )
        }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(60.dp),
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
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize(),
            containerColor = Colors.md_Surface,
        ) {
            selectedItem.forEachIndexed { index,  item ->
                NavigationBarItem(
                    modifier = Modifier.align(Alignment.Top),
                    icon = {Icon(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp),
                        imageVector = if (selectedDestination == item.route)
                            item.selectedIcon
                        else item.unselectedIcon,
                        contentDescription = null) },
                    /*label = { Text(
                        //modifier = Modifier.width(60.dp).height(10.dp),
                        text = item.text,
                        fontSize = 12.sp) },*/
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


@Composable
fun EventsDetails(
    modifier: Modifier = Modifier,
    text: String
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))
            /*FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                image.tags.forEach { tag ->
                    SuggestionChip(
                        label = {
                            Text(text = tag)
                        },
                        onClick = {},
                    )
                }
            }*/
            //Spacer(Modifier.height(8.dp))
        }
    }
}

/*@Composable
fun MapScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Text(text = "uy")
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipGroup(
    items: List<String>,
    defaultSelectedItemIndex:Int = 0,
    selectedItemIcon: ImageVector = Icons.Filled.Done,
    itemIcon: ImageVector = Icons.Filled.Build,
    onSelectedChanged : (Int) -> Unit = {}
){
    var selectedItemIndex by remember { mutableStateOf(defaultSelectedItemIndex) }

    LazyRow(userScrollEnabled = true,
        modifier = Modifier,

    ) {

        items(items.size) { index: Int ->
            FilterChip(
                modifier = Modifier.padding(4.dp),
                selected = items[selectedItemIndex] == items[index],
                onClick = {
                    selectedItemIndex = index
                    onSelectedChanged(index)
                },
                label = { Text(items[index]) },
                leadingIcon = if (items[selectedItemIndex] == items[index]) {
                    {
                        Icon(
                            imageVector = selectedItemIcon,
                            contentDescription = "Localized Description",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    {
                        Icon(
                            imageVector = itemIcon,
                            contentDescription = "Localized description",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PreviewFilterChipGroup() {
    FilterChipGroup(items = listOf("Ближайшие мероприятия", "Без опыта", "Есть опыт", "Очень опытные"),
        onSelectedChanged = {

        })
}
