package com.example.duno

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DunoNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = DunoScreens.EVENTS_SCREEN
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ){
        composable(DunoScreens.EVENTS_SCREEN){
            @Composable
            fun EventsScreen(
                modifier: Modifier = Modifier,
            ) {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold (
                    //modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    bottomBar = {
                        BottomAppBar(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ){
                            SmallFloatingActionButton(
                                onClick = {},
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.secondary
                            ){
                                Icon(imageVector = Icons.Filled.Add, "Small floating action button.")
                            }

                        }
                    }
                ){
                    innerPadding ->
                    Text(
                        modifier = Modifier.padding(innerPadding),
                        text = "Example of a scaffold with a bottom app bar."
                    )

                }
            }
        }
    }
}
