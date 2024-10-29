package com.example.futurefarmers.ui.level

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.futurefarmers.data.repository.MainRepository
import com.example.futurefarmers.data.response.GetLevelConfigResponse
import com.example.futurefarmers.data.response.GetRelayConfigResponse
import com.example.futurefarmers.data.response.UpdateLevelConfigResponse
import com.example.futurefarmers.data.response.UpdateRelayConfigResponse
import com.google.gson.JsonObject

class LevelViewModel(private var repository: MainRepository): ViewModel() {
    private lateinit var levelResponse: LiveData<GetLevelConfigResponse>
    private lateinit var updateLevelResponse: LiveData<UpdateLevelConfigResponse>

    fun getSession(): LiveData<String> {
        return repository.getSession().asLiveData()
    }

    fun getLevel(token: String){
        levelResponse = repository.getLevelConfig(token)
    }

    fun getLevelResponse(): LiveData<GetLevelConfigResponse> {
        return levelResponse
    }

    fun updateLevel(token: String, jsonObject: JsonObject){
        updateLevelResponse = repository.updateLevelConfig(token, jsonObject)
    }
    fun getUpdateLevelResponse(): LiveData<UpdateLevelConfigResponse> {
        return updateLevelResponse
    }
}