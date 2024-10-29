package com.example.futurefarmers.ui.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.futurefarmers.data.repository.MainRepository
import com.example.futurefarmers.data.response.GetPlantResponse
import com.example.futurefarmers.data.response.GetRelayConfigResponse
import com.example.futurefarmers.data.response.UpdateRelayConfigResponse
import com.google.gson.JsonObject

class ConfigViewModel(private var repository: MainRepository): ViewModel() {

    private lateinit var configResponse: LiveData<GetRelayConfigResponse>
    private lateinit var updateConfigResponse: LiveData<UpdateRelayConfigResponse>

    fun getSession(): LiveData<String> {
        return repository.getSession().asLiveData()
    }

    fun getConfig(token: String){
        configResponse = repository.getRelayConfig(token)
    }

    fun getConfigResponse(): LiveData<GetRelayConfigResponse> {
        return configResponse
    }

    fun updateConfig(token: String, jsonObject: JsonObject){
        updateConfigResponse = repository.updateRelayConfig(token, jsonObject)
    }
    fun getUpdateConfigResponse(): LiveData<UpdateRelayConfigResponse> {
        return updateConfigResponse
    }
}