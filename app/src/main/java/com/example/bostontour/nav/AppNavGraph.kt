package com.example.bostontour.nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

/**
 * Simple data for the tour. Each category has places with numeric IDs.
 */
private val Data = mapOf(
    "Museums" to listOf(
        Place(101, "MIT Museum"),
        Place(102, "Museum of Science"),
        Place(103, "Isabella Stewart Gardner Museum")
    ),
    "Parks" to listOf(
        Place(201, "Boston Common"),
        Place(202, "Public Garden"),
        Place(203, "Charles River Esplanade")
    ),
    "Restaurants" to listOf(
        Place(301, "Legal Sea Foods"),
        Place(302, "Union Oyster House"),
        Place(303, "Giulia")
    ),
    "Landmarks" to listOf(
        Place(401, "Faneuil Hall"),
        Place(402, "Old North Church"),
        Place(403, "Bunker Hill Monument")
    )
)

data class Place(val id: Int, val name: String)

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onGoHome: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home.route,
        modifier = modifier
    ) {
        // HOME
        composable(Route.Home.route) {
            HomeScreen(
                onStartTour = { navController.navigate(Route.Categories.route) }
            )
        }

        // CATEGORIES
        composable(Route.Categories.route) {
            CategoriesScreen(
                categories = Data.keys.sorted(),
                onCategory = { category ->
                    // Example of a structured route string using a String argument
                    navController.navigate(Route.List.path(category))
                }
            )
        }

        // LIST (uses String argument)
        composable(
            route = Route.List.route,
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) { entry ->
            val category = entry.arguments?.getString("category").orEmpty()
            val places = Data[category].orEmpty()

            ListScreen(
                category = category,
                places = places,
                onOpen = { placeId ->
                    // Example of structured route using both String and Int arguments
                    navController.navigate(Route.Detail.path(category, placeId))
                }
            )
        }

        // DETAIL (uses String + Int arguments)
        composable(
            route = Route.Detail.route,
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("placeId") { type = NavType.IntType }
            )
        ) { entry ->
            val category = entry.arguments?.getString("category").orEmpty()
            val placeId  = entry.arguments?.getInt("placeId") ?: -1
            val place = Data[category]?.firstOrNull { it.id == placeId }

            DetailScreen(
                category = category,
                place = place,
                // If you wanted to clear stack from inside detail:
                onGoHome = {
                    onGoHome()
                }
            )
        }
    }
}

/* ------------------------ Screens ------------------------ */

@Composable
fun HomeScreen(onStartTour: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Welcome to Boston Tour!", modifier = Modifier.padding(bottom = 8.dp))
        Text("Explore categories to start your tour.")
        Spacer(Modifier.height(16.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onStartTour() }
        ) {
            Text("Click here to start!",
                modifier = Modifier.padding(12.dp),
                textDecoration = TextDecoration.Underline)
        }
    }
}

@Composable
fun CategoriesScreen(categories: List<String>, onCategory: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Choose a Category")
        Spacer(Modifier.height(8.dp))
        Divider()
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 8.dp)) {
            items(categories) { cat ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategory(cat) }
                ) {
                    Text(cat, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Composable
fun ListScreen(category: String, places: List<Place>, onOpen: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("All $category")
        Spacer(Modifier.height(8.dp))
        Divider()
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 8.dp)) {
            items(places, key = { it.id }) { place ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpen(place.id) }
                ) {
                    Text("${place.name} (id=${place.id})", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Composable
fun DetailScreen(category: String, place: Place?, onGoHome: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Category: $category")
        Spacer(Modifier.height(8.dp))
        Divider()
        Spacer(Modifier.height(8.dp))
        if (place == null) {
            Text("Location not found.")
        } else {
            Text("Destination: ${place.name}")
            Text("Location ID: ${place.id}")
            Spacer(Modifier.height(16.dp))
            Text("Tip: Tap the Home icon in the top bar to clear the stack.")
        }

        Spacer(Modifier.height(24.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onGoHome() }
        ) {
            Text("Return to Home",
                modifier = Modifier.padding(12.dp),
                textDecoration = TextDecoration.Underline)
        }
    }
}