package com.example.manajemenkeuangan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val description: String,
    val amount: Double,
    val isExpense: Boolean, // true untuk pengeluaran, false untuk pemasukan
    val photoPath: String? = null,
    val date: Long = System.currentTimeMillis()
)