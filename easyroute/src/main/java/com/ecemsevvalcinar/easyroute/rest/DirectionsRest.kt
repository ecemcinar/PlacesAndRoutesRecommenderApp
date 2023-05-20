package com.ecemsevvalcinar.easyroute.rest

import com.ecemsevvalcinar.easyroute.model.Directions
import com.ecemsevvalcinar.easyroute.EasyRoutesDirections

interface DirectionsRest {

    suspend fun getDirections(easyRoutesDirections: EasyRoutesDirections) : Directions

}