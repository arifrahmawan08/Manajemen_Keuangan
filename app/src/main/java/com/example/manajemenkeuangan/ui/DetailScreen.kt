package com.example.manajemenkeuangan.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.manajemenkeuangan.ui.theme.GreenIncome
import com.example.manajemenkeuangan.ui.theme.RedExpense
import com.example.manajemenkeuangan.ui.viewmodel.AddEditTransactionViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    transactionId: Int,
    viewModel: AddEditTransactionViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val transaction by viewModel.transaction.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Transaksi", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    transaction?.let { currentTransaction ->
                        IconButton(onClick = {
                            navController.navigate("add_edit/${currentTransaction.id}")
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Transaksi")
                        }
                        IconButton(onClick = {
                            coroutineScope.launch {
                                viewModel.deleteTransaction()
                                navController.popBackStack()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus Transaksi")
                        }
                    }
                }
            )
        }
    ) { padding ->
        transaction?.let { currentTransaction ->
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
                maximumFractionDigits = 0
            }
            val formattedAmount = currencyFormat.format(currentTransaction.amount)

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Deskripsi
                Text(
                    text = currentTransaction.description,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                        .format(Date(currentTransaction.date)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Kartu jumlah transaksi
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jumlah",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = formattedAmount,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (currentTransaction.isExpense) RedExpense else GreenIncome
                        )
                    }
                }

                // Foto transaksi jika ada
                currentTransaction.photoPath?.let { uriString ->
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = Uri.parse(uriString)),
                            contentDescription = "Foto Transaksi",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Transaksi tidak ditemukan.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
