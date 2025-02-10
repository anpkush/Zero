package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.LoginRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.LoginResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = LoginRepository()

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(name: String, countryCode: String, mobileNumber: String) {
        viewModelScope.launch {
            try {
                val response = repository.postSendOtp(LoginRequest(countryCode, mobileNumber, name))
                if (response.isSuccessful) {
                    _loginResponse.postValue(response.body())
                } else {
                    _error.postValue("Login failed: ${response.errorBody()?.string() ?: "Unknown error"}")  // **(CHANGED) Show API error**
                }
            } catch (e: Exception) {
                _error.postValue("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}
