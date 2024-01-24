package cz.janbaslar.dictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.janbaslar.dictionary.data.models.ApiResponse
import cz.janbaslar.dictionary.service.SharedPreferencesService
import cz.janbaslar.dictionary.ui.theme.DictionaryTheme
import cz.janbaslar.dictionary.ui.viewmodels.SavedScreen
import cz.janbaslar.dictionary.ui.viewmodels.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    App(SharedPreferencesService(this.application.applicationContext))
                }
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Search : Screen("search_screen", R.string.search, Icons.Filled.Search)
    object Saved : Screen("saved_screen", R.string.saved, Icons.Filled.Bookmarks)
}

@Composable
fun App(sharedPreferencesService: SharedPreferencesService) {
    val navController = rememberNavController()
    val searchedWord = remember { mutableStateOf("") }
    val savedWord = remember { mutableStateOf("") }
    val lastApiResponse = remember { mutableStateOf(ApiResponse.empty()) }

    val items = listOf(
        Screen.Search,
        Screen.Saved,
    )

    Scaffold(
        bottomBar = {
            BottomAppBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = screen.route) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Search.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Search.route) {
                SearchScreen(
                    sharedPreferencesService,
                    searchedWord,
                    lastApiResponse
                )
            }
            composable(Screen.Saved.route) { SavedScreen(sharedPreferencesService, savedWord) }
        }
    }
}