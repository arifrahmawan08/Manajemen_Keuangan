package com.example.manajemenkeuangan.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.manajemenkeuangan.ui.viewmodel.AddEditTransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    navController: NavController,
    viewModel: AddEditTransactionViewModel,
) {
    val isEditMode = viewModel.transactionId != null
    val context = LocalContext.current

    val description by viewModel.description.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val isExpense by viewModel.isExpense.collectAsState()
    val photoPath by viewModel.photoPath.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.savePhotoUri(uri?.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Transaksi" else "Tambah Transaksi") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = description,
                onValueChange = { viewModel.description.value = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { viewModel.amount.value = it },
                label = { Text("Jumlah") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pemasukan")
                Switch(
                    checked = isExpense,
                    onCheckedChange = { viewModel.toggleIsExpense(it) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                Text("Pilih Foto")
            }
            Spacer(modifier = Modifier.height(16.dp))
            photoPath?.let { uriString ->
                Image(
                    painter = rememberAsyncImagePainter(model = Uri.parse(uriString)),
                    contentDescription = "Foto Transaksi",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.saveTransaction()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = description.isNotEmpty() && amount.toDoubleOrNull() != null
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Tambah Transaksi")
            }
        }
    }
}