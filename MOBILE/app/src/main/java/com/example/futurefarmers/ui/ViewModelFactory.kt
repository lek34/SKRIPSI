package com.example.futurefarmers.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.futurefarmers.data.repository.MainRepository
import com.example.futurefarmers.`object`.Injection
import com.example.futurefarmers.ui.config.ConfigViewModel
import com.example.futurefarmers.ui.control.ControlViewModel
import com.example.futurefarmers.ui.level.LevelViewModel
import com.example.futurefarmers.ui.main.MainViewModel
import com.example.futurefarmers.ui.plant.PlantViewModel
import com.example.futurefarmers.ui.setting.SettingViewModel

class ViewModelFactory private constructor(private val repository: MainRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PlantViewModel::class.java) -> {
                PlantViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ConfigViewModel::class.java) -> {
                ConfigViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ControlViewModel::class.java) -> {
                ControlViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LevelViewModel::class.java) -> {
                LevelViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideMainRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}