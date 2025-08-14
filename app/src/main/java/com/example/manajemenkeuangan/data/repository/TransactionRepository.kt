package com.example.manajemenkeuangan.data.repository

import com.example.manajemenkeuangan.data.db.TransactionDao
import com.example.manajemenkeuangan.data.model.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    fun getTransactionById(id: Int): Flow<Transaction?> {
        return transactionDao.getTransactionById(id)
    }

    suspend fun insertOrUpdateTransaction(transaction: Transaction) {
        if (transaction.id == null) {
            insertTransaction(transaction)
        } else {
            updateTransaction(transaction)
        }
    }
}