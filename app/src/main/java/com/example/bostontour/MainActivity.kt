package com.example.bostontour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bostontour.nav.AppNavGraph
import com.example.bostontour.nav.Route

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { BostonTourApp() }
    }
}

@Composable
fun BostonTourApp() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()

    // Title changes with destination
    val title = when (backStack?.destination?.route?.substringBefore("?")?.substringBefore("/")) {
        Route.Categories.base -> "Boston Tour • Categories"
        Route.List.base       -> "Boston Tour • List"
        Route.Detail.base     -> "Boston Tour • Detail"
        else                  -> "Boston Tour • Home"
    }

    // When user taps the Home action, we "lock" the back button at Home
    var homeLocked by remember { mutableStateOf(false) }
    val onGoHome: () -> Unit = {
        homeLocked = true
        // Clear the stack so Home is the only destination
        nav.navigate(Route.Home.route) {
            popUpTo(Route.Home.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    // If user navigates anywhere other than Home, unlock back again
    LaunchedEffect(backStack?.destination?.route) {
        if (backStack?.destination?.route?.substringBefore("?")?.substringBefore("/") != Route.Home.base) {
            homeLocked = false
        }
    }

    // Disable the system back press when "locked" at Home
    BackHandler(enabled = homeLocked) { /* consume back; do nothing */ }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    val isAtHome = backStack?.destination?.route?.substringBefore("?")?.substringBefore("/") == Route.Home.base
                    if (!isAtHome && !homeLocked) {
                        IconButton(onClick = { nav.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onGoHome) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                }
            )
        }
    ) { inner ->
        AppNavGraph(
            navController = nav,
            modifier = Modifier.padding(inner),
            onGoHome = onGoHome // if any screen wants to hard-return Home
        )
    }
}