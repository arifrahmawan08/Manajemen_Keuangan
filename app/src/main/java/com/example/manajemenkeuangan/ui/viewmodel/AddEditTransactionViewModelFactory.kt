package com.example.manajemenkeuangan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manajemenkeuangan.data.pref.UserPreferencesRepository
import com.example.manajemenkeuangan.data.repository.TransactionRepository

class AddEditTransactionViewModelFactory(
    private val repository: TransactionRepository,
    private val transactionId: Int?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditTransactionViewModel(repository, transactionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}