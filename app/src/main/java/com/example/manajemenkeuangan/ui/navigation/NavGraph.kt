package com.example.manajemenkeuangan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.manajemenkeuangan.data.db.AppDatabase
import com.example.manajemenkeuangan.data.pref.UserPreferencesRepository
import com.example.manajemenkeuangan.data.repository.TransactionRepository
import com.example.manajemenkeuangan.data.repository.UserRepository
import com.example.manajemenkeuangan.ui.*
import com.example.manajemenkeuangan.ui.viewmodel.*

@Composable
fun ManajemenKeuanganNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    // Database & Repository
    val database = remember { AppDatabase.getDatabase(context) }
    val transactionRepository = remember { TransactionRepository(database.transactionDao()) }
    val userRepository = remember { UserRepository(database.userDao()) }
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }

    // Cek status login
    val isLoggedIn by userPreferencesRepository.getLoginStatus().collectAsState(initial = false)
    val startDestination = if (isLoggedIn) "main_screen" else "login_screen"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Main Screen pakai MainViewModel
        composable("main_screen") {
            val mainViewModel: MainViewModel = viewModel(
                factory = MainViewModelFactory(
                    transactionRepository,
                    userPreferencesRepository
                )
            )
            MainScreen(navController, mainViewModel)
        }

        // Add / Edit Transaction
        composable("add_edit") {
            AddEditTransactionScreen(
                navController,
                viewModel = viewModel(
                    factory = AddEditTransactionViewModelFactory(transactionRepository, null)
                )
            )
        }

        composable("add_edit/{id}") { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("id")?.toInt()
            AddEditTransactionScreen(
                navController,
                viewModel = viewModel(
                    factory = AddEditTransactionViewModelFactory(transactionRepository, transactionId)
                )
            )
        }

        // Detail Transaction
        composable("detail/{id}") { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("id")?.toInt()
            if (transactionId != null) {
                DetailScreen(
                    navController,
                    transactionId,
                    viewModel = viewModel(
                        factory = AddEditTransactionViewModelFactory(transactionRepository, transactionId)
                    )
                )
            }
        }

        // Login Screen
        composable("login_screen") {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(userRepository, userPreferencesRepository)
            )
            LoginScreen(navController, loginViewModel)
        }

        // Register Screen
        composable("register_screen") {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(userRepository)
            )
            RegisterScreen(navController, registerViewModel)
        }
    }
}
