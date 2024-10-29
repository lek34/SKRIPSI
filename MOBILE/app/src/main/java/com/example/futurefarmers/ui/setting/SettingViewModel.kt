package com.example.futurefarmers.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.futurefarmers.data.repository.MainRepository
import kotlinx.coroutines.launch

class SettingViewModel(private var repository: MainRepository): ViewModel() {

    fun getSession(): LiveData<String> {
        return repository.getSession().asLiveData()
    }
    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }
}