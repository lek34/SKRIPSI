package com.example.futurefarmers.data.response

import com.google.gson.annotations.SerializedName

data class DataResponse(

	@field:SerializedName("tds")
	val tds: Int,

	@field:SerializedName("ph")
	val ph: Any,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("suhu")
	val suhu: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("kelembapan")
	val kelembapan: Int
)
