# CS 501 Assignment 5 Question 3 — BostonTour

## Overview
**BostonTour** is a multi-screen Android app built with **Jetpack Compose Navigation** that takes users on a virtual tour of Boston.  
It demonstrates **deep navigation** across multiple levels — from Home → Categories → List → Detail — while maintaining proper **backstack behavior**, argument passing, and navigation state control.

## Features
- **Home Screen:** Introductory screen to start the Boston tour.  
- **Categories Screen:** Displays tour categories such as *Museums, Parks, Restaurants,* and *Landmarks*.  
- **List Screen:** Lists all locations for the selected category.  
- **Detail Screen:** Shows detailed information about a specific location.  
- **Structured Route Navigation:** Uses both `String` and `Int` arguments to navigate (e.g., `"detail/Museums/101"`).  
- **Stack Management:** Returning to Home clears the backstack using `popUpTo(inclusive = true)`.  
- **Reused TopAppBar:** Common top bar across all screens with dynamic title and Home button.  
- **Back Button Control:** Disables the system back button after returning Home to prevent navigation loops.  
- **UI Styling:** Uses `OutlinedButton` and `Surface` with subtle borders to create a clean, tappable button appearance.

## How to Run
1. Open the project in **Android Studio**.  
2. Make sure your `app/build.gradle.kts` file includes these dependencies:
   ```kotlin
   implementation("androidx.navigation:navigation-compose:2.8.3")
   implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
   implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
   ```
3. Sync Gradle and run the app on an emulator or device.

### App Flow
1. **Home → Categories**  
   Tap the start button to see all Boston tour categories.  
2. **Categories → List**  
   Select a category like “Museums” to view its full list.  
3. **List → Detail**  
   Tap a destination (e.g., *MIT Museum*) to see details.  
4. **Return Home**  
   Use the Home icon or “Return to Home” button — this clears the navigation stack.

### Architecture
| Component | Description |
|------------|-------------|
| `Route` | Sealed class defining each navigation route with argument templates. |
| `AppNavGraph` | Central navigation host managing all composable destinations. |
| `MainActivity` | Scaffold container with `TopAppBar` and back/home logic. |
| `HomeScreen`, `CategoriesScreen`, `ListScreen`, `DetailScreen` | Four distinct screens forming the navigation hierarchy. |

## AI Usage Documentation

### How AI Was Used
AI assistance was used for **debugging UI clarity** and **navigation logic** improvements:
- **Clickable Feedback:**  
  Original version used plain `Surface` with `Modifier.clickable`, making touch targets unclear.  
  AI suggested replacing them with `OutlinedButton` and adding:
  ```kotlin
  border = BorderStroke(1.dp, Color.LightGray)
  ```
  to improve visibility and user interaction.
- **Backstack Logic Verification:**  
  Confirmed correct use of:
  ```kotlin
  nav.navigate(Route.Home.route) {
      popUpTo(Route.Home.route) { inclusive = true }
      launchSingleTop = true
  }
  ```
  ensuring that going “Home” clears the stack entirely.
- **BackHandler Optimization:**  
  AI helped simplify `BackHandler(enabled = homeLocked)` logic to properly disable back navigation only after a full cycle to Home.

### Where AI Misunderstood
AI initially suggested applying animations similar to `Crossfade()` from the previous assignment, but the requirement focused on **stack navigation**, not transitions. After clarification, animations were removed and attention was shifted toward **argument passing** and **stack control**.
