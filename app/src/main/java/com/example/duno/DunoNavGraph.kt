package com.example.duno

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.duno.compose.auth.LoginScreen
import com.example.duno.compose.auth.RegistrationScreen
import com.example.duno.compose.elements.LoadingScreen
import com.example.duno.compose.events.CreateEventScreen
import com.example.duno.compose.events.EventDetailsScreen
import com.example.duno.compose.events.EventsScreen
import com.example.duno.compose.map.MapScreenUI
import com.example.duno.compose.profile.ProfileScreen
import com.example.duno.compose.profile.UserEventsProfile
import com.example.duno.ui.Colors
import com.example.duno.viewmodel.MapViewModel
import com.example.duno.viewmodel.MeetingViewModel
import com.example.duno.viewmodel.UserViewModel
import timber.log.Timber

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DunoNavGraph(
    userViewModel: UserViewModel,
    meetingViewModel: MeetingViewModel,
    mapViewModel: MapViewModel,
    navController: NavHostController,
    isLoggedIn: Boolean,
    userName: String,
    userNickname: String,
    innerPadding: PaddingValues,

    ){
    NavHost(
        navController = navController,
        startDestination = DunoScreens.LOGIN_SCREEN,
        route = "Parent"
    ){
        composable(DunoScreens.EVENTS_SCREEN){
            //navController.navigateSingleTopTo(DunoScreens.EVENTS_SCREEN)
            val coroutineScope = rememberCoroutineScope()
            EventsScreen(
                goToEventDetails = { eventId, showButtonDeleteLike ->
                    navController.navigate("${DunoScreens.ABOUT_EVENT_SCREEN}/$eventId/$showButtonDeleteLike")
                },
                userNickname,
                meetingViewModel,
                userViewModel,
            )
            //username,is logged
        }
        composable(DunoScreens.MAP_SCREEN,deepLinks = listOf( navDeepLink {
            uriPattern = "https://com.example.duno.ru/mapscreen"
            action = Intent.ACTION_VIEW
        } )){
            /*val parentEntry = remember (it){
                navController.getBackStackEntry("Parent")
            }

            val vm:MapViewModel = hiltViewModel(parentEntry)*/
            MapScreenUI(
                goToEvents = {
                    navController.navigateSingleTopTo(DunoScreens.EVENTS_SCREEN)
                },
                goToEventDetails = {eventId, showButtonDeleteLike ->
                    navController.navigate("${DunoScreens.ABOUT_EVENT_SCREEN}/$eventId/$showButtonDeleteLike")
                },
                userNickname,
                meetingViewModel,
                userViewModel,
                mapViewModel,
                innerPadding
            )
        }
        composable(DunoScreens.PROFILE_SCREEN){
            ProfileScreen(
                userViewModel,
                goToSignUp = {
                    navController.navigateSingleTopTo(DunoScreens.LOGIN_SCREEN)
                },
                goToUserEvents ={title ->
                    navController.navigate("${DunoScreens.USER_EVENTS_SCREEN}/$title")
                },
                goToAddEvent ={
                    navController.navigate(DunoScreens.ADD_EDIT_EVENT_SCREEN)
                },
                goToEvents = {
                    navController.navigateSingleTopTo(DunoScreens.EVENTS_SCREEN)
                },
                userName,
                userNickname,
            )
        }
        composable("${DunoScreens.USER_EVENTS_SCREEN}/{title}", arguments = listOf(navArgument("title") { type = NavType.StringType })
        ){
            it.arguments?.getString("title")?.let {title ->
                UserEventsProfile(title = title,
                    userNickname,
                    meetingViewModel,
                    navigateToProfile = {
                        navController.popBackStack()
                    },
                    userViewModel = userViewModel,
                    navigateToEventsDetails = {eventId, showButtonDeleteLike ->
                        navController.navigate("${DunoScreens.ABOUT_EVENT_SCREEN}/$eventId/$showButtonDeleteLike")
                    })
            }
        }
        composable(
            "${DunoScreens.ABOUT_EVENT_SCREEN}/{eventId}/{showButtonDeleteLike}",
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType},
                navArgument("showButtonDeleteLike") { type = NavType.BoolType},
            )
        ){
            val eventId = it.arguments!!.getInt("eventId")
            val showButtonDeleteLike = it.arguments!!.getBoolean("showButtonDeleteLike")
            Timber.e(eventId.toString())
            EventDetailsScreen(
                eventId = eventId,
                onBack = {navController.popBackStack()},
                meetingViewModel,
                userViewModel,
                showButtonDeleteLike,
                userNickname,
                innerPadding
            )
        }
        composable(DunoScreens.ADD_EDIT_EVENT_SCREEN){
            CreateEventScreen(
                userViewModel,
                goToEvents ={
                    navController.previousBackStackEntry
                },
            )
            //username,is logged
        }
        composable(DunoScreens.LOADING_SCREEN){
            LoadingScreen(
                meetingViewModel,
                userViewModel,
                userNickname,
                navigateToEvents ={
                    navController.navigateSingleTopTo(DunoScreens.EVENTS_SCREEN)
                },
            )
            //username,is logged
        }
        composable(DunoScreens.LOGIN_SCREEN){
            LoginScreen(
                isLoggedIn,
                navController,
                userViewModel,
                goToMainScreen = {
                     navController.navigateSingleTopTo(DunoScreens.LOADING_SCREEN)
                }, {},
                onSignUpClick = {
                    navController.navigateSingleTopTo(DunoScreens.SIGNUP_SCREEN)
                })
        }
        composable(DunoScreens.SIGNUP_SCREEN){
            RegistrationScreen(
                isLoggedIn,
                navController,
                userViewModel,
                goToMainScreen = {
                    navController.navigateSingleTopTo(DunoScreens.LOADING_SCREEN)
                },
                onSignUpClick = {
                    userViewModel.userRegistration(it)

                },
                onLoginClick = {
                    navController.navigateSingleTopTo(DunoScreens.LOGIN_SCREEN)
            },{},{})
        }
    }
}


@Composable
fun Screen(
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel = hiltViewModel(),
    meetingViewModel: MeetingViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    //mapViewModel: MapViewModel= hiltViewModel()
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    val userName by userViewModel.userName.collectAsState()
    val userNickname by userViewModel.userNickname.collectAsState()
    //val userState = mutableListOf(isLoggedIn, userName, userPassword)
    var selectedDestination = navBackStackEntry?.destination?.route ?: DunoScreens.LOGIN_SCREEN
    LaunchedEffect(!isLoggedIn) {
        //TODO чтобы несколько раз не вылетал logged
        Timber.tag("Login State activate")
            .e("Is logged:${isLoggedIn.toString()}, userName:${userName.toString()}")
        selectedDestination = navBackStackEntry?.destination?.route ?: DunoScreens.SIGNUP_SCREEN
//            if (isLoggedIn) navBackStackEntry?.destination?.route ?: DunoScreens.EVENTS_SCREEN
            //else

    }
    LaunchedEffect(isLoggedIn) {
        //TODO чтобы несколько раз не вылетал logged
        Timber.tag("Login State activate")
            .e("Is logged:${isLoggedIn.toString()}, userName:${userName.toString()}")
        selectedDestination = navBackStackEntry?.destination?.route ?: DunoScreens.LOADING_SCREEN
//            if (isLoggedIn) navBackStackEntry?.destination?.route ?: DunoScreens.EVENTS_SCREEN
        //else

    }
    MainApp(userViewModel,meetingViewModel,mapViewModel, selectedDestination, navController, isLoggedIn, userName, userNickname)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    userViewModel: UserViewModel,
    meetingViewModel: MeetingViewModel,
    mapViewModel: MapViewModel,
    selectedDestination: String,
    navController: NavHostController,
    isLoggedIn: Boolean,
    userName: String,
    userNickname: String
) {
    val noBottom = listOf(DunoScreens.SIGNUP_SCREEN, DunoScreens.LOGIN_SCREEN, DunoScreens.LOADING_SCREEN, DunoScreens.ADD_EDIT_EVENT_SCREEN)
    Scaffold (
        topBar = {if (selectedDestination == DunoScreens.MAP_SCREEN) {
            TopAppBar(
                modifier = Modifier.height(60.dp),
                colors = topAppBarColors(
                    containerColor = Colors.md_Surface,
                    titleContentColor = Colors.md_Background,
                ),
                title = {
                    Box(modifier = Modifier.fillMaxSize()){
                        Text("Мероприятия",
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                }
            )
        }
        },
        bottomBar = {
            if (!noBottom.contains(selectedDestination))
            {
                HorizontalDivider(Modifier.fillMaxWidth().height(2.dp))
                BottomAppBar(
                    modifier = Modifier.height(60.dp),
                    containerColor = Colors.md_Surface,
                ) {
                    CommonUI(
                        navController = navController,
                        selectedDestination = selectedDestination,
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DunoNavGraph(
                userViewModel,meetingViewModel,mapViewModel, navController, isLoggedIn, userName, userNickname, innerPadding
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
    var imageVec = selectedItem[1].selectedIcon
    Box(modifier = Modifier.fillMaxSize()) {
        NavigationBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxSize(),
            containerColor = Colors.md_Surface,
        ) {
            selectedItem.forEachIndexed { index,  item ->
                when (selectedDestination) {
                    item.route -> {
                        imageVec =item.selectedIcon
                    }
                    DunoScreens.ADD_EDIT_EVENT_SCREEN -> {
                        imageVec =item.selectedIcon
                    }
                    else -> item.unselectedIcon
                }
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
    if (route == DunoScreens.LOGIN_SCREEN) {
        this.navigate(route) {
            popUpTo(
                route
            ) {
                saveState = false
            }
            launchSingleTop = true
            restoreState = false
        }
    }else{
        this.navigate(route) {
            popUpTo(
                this@navigateSingleTopTo.graph.findStartDestination().id
            ) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }




@Preview
@Composable
fun ScreenPreview(){
    //MainApp(userViewModel, selectedDestination, navController, isLoggedIn, userName, userPassword)
}