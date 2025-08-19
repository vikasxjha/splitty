package com.splitwise.easy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val totalAmount: Double,
    val currency: String,
    val category: String,
    val date: Long,
    val createdBy: String,
    val paidBy: String,
    val splitMethod: String,
    val notes: String? = null,
    val receiptUri: String? = null,
    val isSettled: Boolean = false
)

