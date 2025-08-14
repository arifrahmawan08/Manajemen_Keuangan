package com.example.manajemenkeuangan.data.db

import androidx.room.*
import com.example.manajemenkeuangan.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM 'transaction' ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM 'transaction' WHERE id = :transactionId")
    fun getTransactionById(transactionId: Int): Flow<Transaction?>
}