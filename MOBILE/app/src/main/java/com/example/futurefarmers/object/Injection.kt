package com.example.futurefarmers.`object`

import android.content.Context
import com.example.futurefarmers.data.preferences.UserPreference
import com.example.futurefarmers.data.preferences.dataStore
import com.example.futurefarmers.data.remote.auth.AuthConfig
import com.example.futurefarmers.data.remote.config.ApiConfig
import com.example.futurefarmers.data.repository.AuthRepository
import com.example.futurefarmers.data.repository.MainRepository

object Injection {
    fun provideMainRepository(context: Context): MainRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return MainRepository.getInstance(pref,apiService)
    }

    fun provideAuthRepository(context: Context): AuthRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val authService = AuthConfig.getApiService()
        return AuthRepository.getInstance(pref,authService)
    }
}