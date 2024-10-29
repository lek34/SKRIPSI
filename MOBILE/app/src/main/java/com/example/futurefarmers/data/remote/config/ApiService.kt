package com.example.futurefarmers.data.remote.config

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
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    //dashboard
    @GET("api/v1/dashboard")
    fun getData(@Header("Authorization") value: String): Call<DataResponse>

    //plant
    @GET("api/v1/plant")
    fun getPlant(@Header("Authorization") value: String): Call<GetPlantResponse>
    @POST("api/v1/plant")
    fun postPlant(@Header("Authorization") value: String,@Body raw: JsonObject,): Call<PostPlantResponse>

    //relay config
    @GET("api/v1/getrelayconfig")
    fun getRelayConfig(@Header("Authorization") value: String): Call<GetRelayConfigResponse>
    @PUT("api/v1/updaterelayconfig")
    fun updateRelayConfig(@Header("Authorization") value: String,@Body raw: JsonObject): Call<UpdateRelayConfigResponse>

    //level config
    @GET("api/v1/getlevelconfig")
    fun getLevelConfig(@Header("Authorization") value: String): Call<GetLevelConfigResponse>
    @PUT("api/v1/updatelevelconfig")
    fun updateLevelConfig(@Header("Authorization") value: String,@Body raw: JsonObject): Call<UpdateLevelConfigResponse>

}