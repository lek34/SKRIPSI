package com.example.futurefarmers.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
