package com.splitwise.easy.data

data class Transaction(
    val id: String,
    val title: String,
    val amountCents: Long,
    val participants: Int,
    val dateText: String,
    val isPaid: Boolean
)
