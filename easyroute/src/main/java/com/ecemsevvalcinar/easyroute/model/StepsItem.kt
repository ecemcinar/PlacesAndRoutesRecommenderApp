package com.ecemsevvalcinar.easyroute.model

import com.ecemsevvalcinar.easyroute.model.Distance
import com.ecemsevvalcinar.easyroute.model.Duration
import com.ecemsevvalcinar.easyroute.model.EndLocation
import com.ecemsevvalcinar.easyroute.model.Polyline
import com.ecemsevvalcinar.easyroute.model.StartLocation
import com.google.gson.annotations.SerializedName

data class StepsItem(

    @field:SerializedName("duration")
	val duration: Duration? = null,

    @field:SerializedName("start_location")
	val startLocation: StartLocation? = null,

    @field:SerializedName("distance")
	val distance: Distance? = null,

    @field:SerializedName("travel_mode")
	val travelMode: String? = null,

    @field:SerializedName("html_instructions")
	val htmlInstructions: String? = null,

    @field:SerializedName("end_location")
	val endLocation: EndLocation? = null,

    @field:SerializedName("maneuver")
	val maneuver: String? = null,

    @field:SerializedName("polyline")
	val polyline: Polyline? = null
)