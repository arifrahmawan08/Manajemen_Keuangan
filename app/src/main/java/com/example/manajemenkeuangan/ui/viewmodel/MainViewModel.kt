package com.example.manajemenkeuangan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.data.pref.UserPreferencesRepository
import com.example.manajemenkeuangan.data.repository.TransactionRepository
import com.example.manajemenkeuangan.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: TransactionRepository,
    private val userPref: UserPreferencesRepository
) : ViewModel() {

    val allTransactions: Flow<List<Transaction>> = repository.allTransactions

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPref.clearUserPreferences()
        }
    }
}

