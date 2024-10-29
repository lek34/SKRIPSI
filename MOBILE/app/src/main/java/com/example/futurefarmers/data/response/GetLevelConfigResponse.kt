package com.example.futurefarmers.data.response

import com.google.gson.annotations.SerializedName

data class GetLevelConfigResponse(

	@field:SerializedName("tds")
	val tds: Int? = null,

	@field:SerializedName("ph_high")
	val phHigh: Int? = null,

	@field:SerializedName("ph_low")
	val phLow: Any? = null,

	@field:SerializedName("temperature_low")
	val temperatureLow: Int? = null,

	@field:SerializedName("humidity")
	val humidity: Int? = null,

	@field:SerializedName("temperature_high")
	val temperatureHigh: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
