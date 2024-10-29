package com.example.futurefarmers.data.response

import com.google.gson.annotations.SerializedName

data class RelayResponse(

	@field:SerializedName("Relay2_is")
	val relay2Is: String? = null,

	@field:SerializedName("Relay1_is")
	val relay1Is: String? = null,

	@field:SerializedName("Relay6_is")
	val relay6Is: String? = null,

	@field:SerializedName("Relay3_is")
	val relay3Is: String? = null,

	@field:SerializedName("is_sync")
	val isSync: String? = null,

	@field:SerializedName("Relay4_is")
	val relay4Is: String? = null,

	@field:SerializedName("Relay5_is")
	val relay5Is: String? = null
)
