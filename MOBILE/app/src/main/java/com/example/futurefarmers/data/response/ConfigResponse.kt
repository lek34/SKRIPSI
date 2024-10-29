package com.example.futurefarmers.data.response

import com.google.gson.annotations.SerializedName

data class ConfigResponse(

	@field:SerializedName("ph_down")
	val phDown: Int? = null,

	@field:SerializedName("fan")
	val fan: Int? = null,

	@field:SerializedName("nut_B")
	val nutB: Int? = null,

	@field:SerializedName("light")
	val light: Int? = null,

	@field:SerializedName("ph_up")
	val phUp: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("nut_a")
	val nutA: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("is_sync")
	val isSync: Int? = null
)
