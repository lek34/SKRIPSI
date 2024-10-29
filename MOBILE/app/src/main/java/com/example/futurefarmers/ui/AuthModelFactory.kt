package com.example.futurefarmers.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.futurefarmers.data.repository.AuthRepository
import com.example.futurefarmers.`object`.Injection
import com.example.futurefarmers.ui.login.LoginViewModel
import com.example.futurefarmers.ui.main.MainViewModel

class AuthModelFactory private constructor(private val repository: AuthRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: AuthModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): AuthModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = AuthModelFactory(Injection.provideAuthRepository(context))
                }
            }
            return INSTANCE as AuthModelFactory
        }
    }
}