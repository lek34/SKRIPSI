package com.example.futurefarmers.data.remote.auth

import com.example.futurefarmers.data.remote.config.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthConfig {
    companion object{
        fun getApiService(): AuthService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)

            val retrofit = Retrofit.Builder()
                .baseUrl("http://202.10.36.154/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
            return retrofit.create(AuthService::class.java)
        }
    }
}