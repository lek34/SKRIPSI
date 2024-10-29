package com.example.futurefarmers.data.response

import com.google.gson.annotations.SerializedName

data class UpdateRelayConfigResponse(

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
