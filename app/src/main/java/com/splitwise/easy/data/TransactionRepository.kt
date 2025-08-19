package com.splitwise.easy.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object TransactionRepository {
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    fun set(transactions: List<Transaction>) {
        _transactions.value = transactions
    }

    fun add(tx: Transaction) {
        _transactions.value = _transactions.value + tx
    }

    fun clear() {
        _transactions.value = emptyList()
    }
}
