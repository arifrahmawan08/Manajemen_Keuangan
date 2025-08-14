package com.example.manajemenkeuangan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manajemenkeuangan.data.pref.UserPreferencesRepository
import com.example.manajemenkeuangan.data.repository.TransactionRepository

class MainViewModelFactory(
    private val repository: TransactionRepository,
    private val userPref: UserPreferencesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository, userPref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}