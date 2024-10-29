package com.example.futurefarmers.ui.plant


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.futurefarmers.data.repository.MainRepository
import com.example.futurefarmers.data.response.LoginResponse
import com.example.futurefarmers.data.response.PostPlantResponse
import com.google.gson.JsonObject

class PlantViewModel(private val repository: MainRepository): ViewModel() {
    private lateinit var postPlantResponse: LiveData<PostPlantResponse>
    fun getSession(): LiveData<String>{
        return repository.getSession().asLiveData()
    }
    fun addPlant(token : String,jsonObject: JsonObject){
        postPlantResponse = repository.addPlant(token,jsonObject)
    }
    fun getPostPlantResponse(): LiveData<PostPlantResponse> {
        return postPlantResponse
    }
}