package com.example.futurefarmers.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futurefarmers.data.repository.AuthRepository
import com.example.futurefarmers.data.repository.MainRepository
import com.example.futurefarmers.data.response.LoginResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository):ViewModel() {
    private lateinit var loginResponse: LiveData<LoginResponse>

    fun login(jsonObject: JsonObject){
        loginResponse = repository.login(jsonObject)
    }
    fun getLoginResponse(): LiveData<LoginResponse>{
        return loginResponse
    }

    fun saveSession(token: String){
        viewModelScope.launch {
            repository.saveSession(token )
        }
    }
}