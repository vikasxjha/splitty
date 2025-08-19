package com.splitwise.easy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitwise.easy.data.User
import com.splitwise.easy.repository.SplittyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FriendsUiState(
    val friends: List<User> = emptyList(),
    val groups: List<String> = emptyList(),
    val isLoading: Boolean = false
)

class FriendsViewModel(private val repository: SplittyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()

    init {
        loadFriends()
    }

    private fun loadFriends() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val friends = repository.getAllUsers()
                val groups = listOf("Roommates", "Work Lunch Group", "Weekend Trip") // Mock data

                _uiState.value = _uiState.value.copy(
                    friends = friends,
                    groups = groups,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun addFriend(name: String, email: String? = null, phone: String? = null) {
        viewModelScope.launch {
            try {
                val user = User(
                    name = name,
                    email = email,
                    phone = phone,
                    addedDate = System.currentTimeMillis()
                )
                repository.insertUser(user)
                loadFriends() // Refresh the list
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
