package com.example.futurefarmers.ui.control

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.futurefarmers.data.repository.MainRepository

class ControlViewModel(private var repository: MainRepository): ViewModel(){
    fun getSession(): LiveData<String> {
        return repository.getSession().asLiveData()
    }

}