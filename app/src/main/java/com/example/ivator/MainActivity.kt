package com.example.ivator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ivator.ui.theme.IvatorTheme
import com.example.ivator.viewModels.ServoControlViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(64)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IvatorTheme {
                AppNav()
            }
        }
    }
}


@RequiresApi(64)
@Composable
fun AppNav(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "patientinfo"){
        composable(route = "patientinfo"){
            Patientinfo(navController)
        }

        composable(route = "home/{name}/{age}/{gender}/{liquid}/{flowrate}/{driprate}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                },
                navArgument("age") {
                    type = NavType.StringType
                },
                navArgument("gender") {
                    type = NavType.StringType
                },
                navArgument("liquid") {
                    type = NavType.StringType
                },
                navArgument("flowrate") {
                    type = NavType.StringType
                },
                navArgument("driprate") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            val age = backStackEntry.arguments?.getString("age")
            val gender = backStackEntry.arguments?.getString("gender")
            val liquid = backStackEntry.arguments?.getString("liquid")
            val flowrate = backStackEntry.arguments?.getString("flowrate")
            val driprate = backStackEntry.arguments?.getString("driprate")
            val servoViewModel: ServoControlViewModel = viewModel()

            HomeScreen(
                name = name,
                age = age,
               gender = gender,
                liquid = liquid,
                flowrate = flowrate,
                driprate = driprate,
                servoViewModel = servoViewModel
            )

        }

    }
}