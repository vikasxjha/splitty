package com.splitwise.easy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitwise.easy.data.Bill
import com.splitwise.easy.data.Participant
import com.splitwise.easy.data.User
import com.splitwise.easy.repository.SplittyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class AddBillUiState(
    val title: String = "",
    val amount: String = "",
    val category: String = "Food",
    val description: String = "",
    val selectedFriends: Set<String> = emptySet(),
    val splitMethod: String = "Equal",
    val isLoading: Boolean = false
)

class AddBillViewModel(private val repository: SplittyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBillUiState())
    val uiState: StateFlow<AddBillUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateAmount(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount)
    }

    fun updateCategory(category: String) {
        _uiState.value = _uiState.value.copy(category = category)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun toggleFriend(friendId: String) {
        val currentSelected = _uiState.value.selectedFriends
        val newSelected = if (friendId in currentSelected) {
            currentSelected - friendId
        } else {
            currentSelected + friendId
        }
        _uiState.value = _uiState.value.copy(selectedFriends = newSelected)
    }

    fun updateSplitMethod(method: String) {
        _uiState.value = _uiState.value.copy(splitMethod = method)
    }

    fun createBill() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val currentState = _uiState.value
                val billId = UUID.randomUUID().toString()

                // Create bill
                val bill = Bill(
                    id = billId,
                    title = currentState.title,
                    totalAmount = currentState.amount.toDoubleOrNull() ?: 0.0,
                    currency = "USD",
                    category = currentState.category,
                    date = System.currentTimeMillis(),
                    createdBy = "current_user", // Would be actual user ID
                    paidBy = "current_user",
                    splitMethod = currentState.splitMethod,
                    notes = currentState.description
                )

                repository.insertBill(bill)

                // Create participants
                val totalAmount = bill.totalAmount
                val participantCount = currentState.selectedFriends.size + 1 // +1 for current user
                val amountPerPerson = totalAmount / participantCount

                // Add current user as participant
                repository.insertParticipant(
                    Participant(
                        billId = billId,
                        userId = "current_user",
                        name = "You",
                        amountOwed = amountPerPerson,
                        amountPaid = totalAmount // User who created pays upfront
                    )
                )

                // Add selected friends as participants
                currentState.selectedFriends.forEach { friendId ->
                    repository.insertParticipant(
                        Participant(
                            billId = billId,
                            userId = friendId,
                            name = friendId, // Would be actual friend name
                            amountOwed = amountPerPerson
                        )
                    )
                }

                _uiState.value = _uiState.value.copy(isLoading = false)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
