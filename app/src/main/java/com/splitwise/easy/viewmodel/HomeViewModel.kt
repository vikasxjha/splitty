package com.splitwise.easy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitwise.easy.data.Bill
import com.splitwise.easy.data.User
import com.splitwise.easy.repository.SplittyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val totalOwedToYou: Double = 0.0,
    val totalYouOwe: Double = 0.0,
    val netBalance: Double = 0.0,
    val recentTransactions: List<Bill> = emptyList(),
    val frequentContacts: List<User> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel(private val repository: SplittyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val recentTransactions = repository.getRecentTransactions(5)
                val allUsers = repository.getAllUsers()

                // Calculate balances (simplified)
                val totalOwed = recentTransactions.sumOf { it.totalAmount / 2 }
                val totalYouOwe = recentTransactions.sumOf { it.totalAmount / 3 }
                val netBalance = totalOwed - totalYouOwe

                _uiState.value = _uiState.value.copy(
                    totalOwedToYou = totalOwed,
                    totalYouOwe = totalYouOwe,
                    netBalance = netBalance,
                    recentTransactions = recentTransactions,
                    frequentContacts = allUsers.take(5),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun refreshData() {
        loadHomeData()
    }
}
