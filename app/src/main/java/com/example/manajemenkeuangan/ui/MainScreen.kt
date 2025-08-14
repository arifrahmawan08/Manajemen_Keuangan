package com.example.manajemenkeuangan.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.manajemenkeuangan.data.pref.UserPreferencesRepository
import com.example.manajemenkeuangan.data.repository.TransactionRepository
import com.example.manajemenkeuangan.ui.theme.RedExpense
import com.example.manajemenkeuangan.ui.theme.GreenIncome
import com.example.manajemenkeuangan.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val transactions by viewModel.allTransactions.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    val totalBalance by remember {
        derivedStateOf {
            transactions.sumOf { if (it.isExpense) -it.amount else it.amount }
        }
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        maximumFractionDigits = 0
    }
    val formattedBalance = currencyFormat.format(totalBalance)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Manajemen Keuangan",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            viewModel.logout()
                            navController.navigate("login_screen") {
                                popUpTo("main_screen") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_edit") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Transaksi")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Kartu saldo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Total Saldo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = formattedBalance,
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (totalBalance >= 0) GreenIncome else RedExpense
                    )
                }
            }

            Text("Riwayat Transaksi", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Belum ada transaksi.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(transactions) { transaction ->
                        val transactionAmountColor = if (transaction.isExpense) RedExpense else GreenIncome
                        val formattedTransactionAmount = currencyFormat.format(transaction.amount)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("detail/${transaction.id}")
                                },
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        transaction.description,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                                            .format(Date(transaction.date)),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = if (transaction.isExpense) "-$formattedTransactionAmount" else "+$formattedTransactionAmount",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = transactionAmountColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
