package com.example.futurefarmers.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.futurefarmers.data.preferences.UserPreference
import com.example.futurefarmers.data.remote.config.ApiService
import com.example.futurefarmers.data.response.ConfigResponse
import com.example.futurefarmers.data.response.DataResponse
import com.example.futurefarmers.data.response.GetLevelConfigResponse
import com.example.futurefarmers.data.response.GetPlantResponse
import com.example.futurefarmers.data.response.GetRelayConfigResponse
import com.example.futurefarmers.data.response.PostPlantResponse
import com.example.futurefarmers.data.response.RelayResponse
import com.example.futurefarmers.data.response.UpdateLevelConfigResponse
import com.example.futurefarmers.data.response.UpdateRelayConfigResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(private val userPreference: UserPreference, private val apiService: ApiService) {

    //get session
    fun getSession(): Flow<String> {
        return userPreference.getSession()
    }
    //untuk logout
    suspend fun logout(){
        userPreference.logout()
    }
    //untuk tampilan dashboard
    fun getData(token:String): LiveData<DataResponse> {
        val dashboardLiveData = MutableLiveData<DataResponse>()

        apiService.getData(token).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    dashboardLiveData.value = data
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return dashboardLiveData
    }

    fun addPlant(token: String, jsonObject: JsonObject): LiveData<PostPlantResponse> {
        val postPlantLiveData = MutableLiveData<PostPlantResponse>()

        apiService.postPlant(token,jsonObject).enqueue(object : Callback<PostPlantResponse> {
            override fun onResponse(call: Call<PostPlantResponse>, response: Response<PostPlantResponse>) {
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    postPlantLiveData.value = data
                }
            }

            override fun onFailure(call: Call<PostPlantResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return postPlantLiveData
    }

    fun getPlant(token: String): LiveData<GetPlantResponse> {
        val getPlantResponse = MutableLiveData<GetPlantResponse>()

        apiService.getPlant(token).enqueue(object : Callback<GetPlantResponse> {
            override fun onResponse(call: Call<GetPlantResponse>, response: Response<GetPlantResponse>) {
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    getPlantResponse.value = data
                }
            }

            override fun onFailure(call: Call<GetPlantResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return getPlantResponse
    }

    fun getRelayConfig(token: String): LiveData<GetRelayConfigResponse> {
        val getRelayConfigResponse = MutableLiveData<GetRelayConfigResponse>()

        apiService.getRelayConfig(token).enqueue(object : Callback<GetRelayConfigResponse> {
            override fun onResponse(call: Call<GetRelayConfigResponse>, response: Response<GetRelayConfigResponse>) {
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    getRelayConfigResponse.value = data
                }
            }

            override fun onFailure(call: Call<GetRelayConfigResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return getRelayConfigResponse
    }

    fun updateRelayConfig(token: String, jsonObject: JsonObject): LiveData<UpdateRelayConfigResponse> {
        val updateRelayConfigResponse = MutableLiveData<UpdateRelayConfigResponse>()

        apiService.updateRelayConfig(token, jsonObject).enqueue(object : Callback<UpdateRelayConfigResponse> {
            override fun onResponse(call: Call<UpdateRelayConfigResponse>, response: Response<UpdateRelayConfigResponse>) {
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    updateRelayConfigResponse.value = data
                }
            }

            override fun onFailure(call: Call<UpdateRelayConfigResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return updateRelayConfigResponse
    }

    //level config
    fun getLevelConfig(token: String): LiveData<GetLevelConfigResponse> {
        val getLevelConfigResponse = MutableLiveData<GetLevelConfigResponse>()

        apiService.getLevelConfig(token).enqueue(object : Callback<GetLevelConfigResponse> {
            override fun onResponse(call: Call<GetLevelConfigResponse>, response: Response<GetLevelConfigResponse>) {
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    getLevelConfigResponse.value = data
                }
            }

            override fun onFailure(call: Call<GetLevelConfigResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return getLevelConfigResponse
    }

    fun updateLevelConfig(token: String, jsonObject: JsonObject): LiveData<UpdateLevelConfigResponse> {
        val updateLevelConfigResponse = MutableLiveData<UpdateLevelConfigResponse>()

        apiService.updateLevelConfig(token, jsonObject).enqueue(object : Callback<UpdateLevelConfigResponse> {
            override fun onResponse(call: Call<UpdateLevelConfigResponse>, response: Response<UpdateLevelConfigResponse>) {
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    updateLevelConfigResponse.value = data
                }
            }

            override fun onFailure(call: Call<UpdateLevelConfigResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

        return updateLevelConfigResponse
    }

    companion object {
        const val TAG="MainRepository"
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(userPreference,apiService)
            }.also { instance = it }
    }
}