package com.example.perpustakaan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.perpustakaan.database.AppDatabase
import com.example.perpustakaan.screens.Screens
import com.example.perpustakaan.screens.editScreen.EditScreen
import com.example.perpustakaan.screens.editScreen.EditScreenViewModel
import com.example.perpustakaan.screens.homeScreen.HomeScreen
import com.example.perpustakaan.screens.homeScreen.HomeScreenViewModel
import com.example.perpustakaan.screens.newScreen.NewScreen
import com.example.perpustakaan.ui.theme.PerpustakaanTheme
import com.example.perpustakaan.screens.newScreen.NewScreenViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerpustakaanTheme {

                // Instantiate the app database
                val appDatabase = AppDatabase.buildAppDatabase(applicationContext)
                val seriesDao = appDatabase.getSeriesDao()

                // Instantiate the navController
                val navController = rememberNavController()

                // Instantiate the view models - the grossest coding possible with compose
                val homeViewModel = ViewModelProvider(this, HomeScreenViewModel.Companion.Factory(seriesDao))[HomeScreenViewModel::class.java]
                val newViewModel = ViewModelProvider(this, NewScreenViewModel.Companion.Factory(seriesDao))[NewScreenViewModel::class.java]
                val editViewModel = ViewModelProvider(this, EditScreenViewModel.Companion.Factory(seriesDao))[EditScreenViewModel::class.java]

                // Create each screen composable
                NavHost(
                    navController = navController,
                    startDestination = Screens.Home
                ) {

                    composable<Screens.Home> { HomeScreen(homeViewModel, navController) }
                    composable<Screens.New>  { NewScreen(newViewModel, navController) }
                    composable<Screens.Edit> {
                        val uid = it.toRoute<Screens.Edit>().uid
                        EditScreen(editViewModel, navController, uid)
                    }

                }

            }
        }
    }

}