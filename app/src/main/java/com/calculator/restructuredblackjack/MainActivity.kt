package com.calculator.restructuredblackjack

import Model.Data.Player
import Model.Data.Routes
import Model.Ui.MainMenu
import Model.Ui.MultiplayerScreen
import Model.Ui.SinglePlayerScreen
import Model.Ui.ViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calculator.restructuredblackjack.ui.theme.RestructuredBlackJackTheme

class MainActivity : ComponentActivity() {
    private val viewModel : ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestructuredBlackJackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.MainMenu.route){
                        composable(Routes.MainMenu.route){
                            MainMenu(navController, viewModel)
                        }
                        composable(Routes.MultiplayerScreen.route){
                            MultiplayerScreen(navController,viewModel)
                        }
                        composable(Routes.SinglePlayerScreen.route){
                            SinglePlayerScreen(navController,viewModel)
                        }

                    }
                }
            }
        }
    }
}
