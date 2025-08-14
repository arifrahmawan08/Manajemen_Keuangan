package com.example.manajemenkeuangan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.data.model.Transaction
import com.example.manajemenkeuangan.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class AddEditTransactionViewModel(
    private val repository: TransactionRepository,
    val transactionId: Int?
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction: StateFlow<Transaction?> = _transaction

    val description = MutableStateFlow("")
    val amount = MutableStateFlow("")
    val isExpense = MutableStateFlow(true)
    val photoPath = MutableStateFlow<String?>(null)

    init {
        if (transactionId != null) {
            viewModelScope.launch {
                repository.getTransactionById(transactionId).collect {
                    it?.let {
                        _transaction.value = it
                        description.value = it.description
                        amount.value = it.amount.toString()
                        isExpense.value = it.isExpense
                        photoPath.value = it.photoPath
                    }
                }
            }
        }
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val transactionToSave = _transaction.value?.copy(
                description = description.value,
                amount = amount.value.toDoubleOrNull() ?: 0.0,
                isExpense = isExpense.value,
                photoPath = photoPath.value,
                date = Date().time
            ) ?: Transaction(
                description = description.value,
                amount = amount.value.toDoubleOrNull() ?: 0.0,
                isExpense = isExpense.value,
                photoPath = photoPath.value,
                date = Date().time
            )
            repository.insertOrUpdateTransaction(transactionToSave)
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            _transaction.value?.let { repository.deleteTransaction(it) }
        }
    }

    fun savePhotoUri(uriString: String?) {
        photoPath.value = uriString
    }

    fun toggleIsExpense(checked: Boolean) {
        isExpense.value = checked
    }
}

