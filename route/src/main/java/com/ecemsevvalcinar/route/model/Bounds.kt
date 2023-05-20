package com.ecemsevvalcinar.route.model

import com.google.gson.annotations.SerializedName

data class Bounds(

	@SerializedName("southwest")
	val southwest: Southwest? = null,

	@field:SerializedName("northeast")
	val northeast: Northeast? = null
)