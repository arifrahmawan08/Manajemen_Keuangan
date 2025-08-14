package com.example.manajemenkeuangan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.data.user.User
import com.example.manajemenkeuangan.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _errorMessage.value = null

            if (password != confirmPassword) {
                _errorMessage.value = "Password tidak cocok."
                return@launch
            }

            val existingUser = userRepository.getUserByEmail(email)
            if (existingUser != null) {
                _errorMessage.value = "Email sudah terdaftar."
                return@launch
            }

            val newUser = User(email = email, password = password)
            userRepository.insertUser(newUser)
            _isRegistered.value = true
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}

