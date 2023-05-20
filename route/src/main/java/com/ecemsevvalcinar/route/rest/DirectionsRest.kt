package com.ecemsevvalcinar.route.rest

import com.ecemsevvalcinar.route.model.Directions
import com.ecemsevvalcinar.route.EasyRoutesDirections

interface DirectionsRest {

    suspend fun getDirections(easyRoutesDirections: EasyRoutesDirections) : Directions

}