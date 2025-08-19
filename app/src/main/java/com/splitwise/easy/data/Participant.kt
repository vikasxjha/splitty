package com.splitwise.easy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "participants")
data class Participant(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val billId: String,
    val userId: String,
    val name: String,
    val amountOwed: Double,
    val amountPaid: Double = 0.0,
    val isPaid: Boolean = false,
    val paymentDate: Long? = null
)

