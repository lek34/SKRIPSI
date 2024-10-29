package com.example.futurefarmers.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.futurefarmers.data.repository.MainRepository
import com.example.futurefarmers.data.response.DataResponse
import com.example.futurefarmers.data.response.GetPlantResponse

class MainViewModel(private val repository: MainRepository): ViewModel(){
    companion object{
        const val TAG ="MainViewModel"
    }

    private lateinit var dataPlant: LiveData<GetPlantResponse>
    private lateinit var dataDashboard: LiveData<DataResponse>
    fun dashboard(token: String){
        dataDashboard = repository.getData(token)
    }
    fun tanaman(token: String){
        dataPlant = repository.getPlant(token)
    }

    fun getDataPlant(): LiveData<GetPlantResponse> {
        return dataPlant
    }
    fun getDataDashboard(): LiveData<DataResponse> {
        return dataDashboard
    }
    fun getSession(): LiveData<String>{
        return repository.getSession().asLiveData()
    }
}