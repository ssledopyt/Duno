package com.example.duno.compose.elements

import com.example.duno.db.ApiLocationOfSP
import timber.log.Timber

fun searchingTitleGeo(locations: List<ApiLocationOfSP>?,
                      eventGeo: List<Float>):String{
    locations?.forEach(){
        if (it.geoMarker[0] == eventGeo[0] && it.geoMarker[1] == eventGeo[1]) return it.nameClub
    }
    return "Неизвестный клуб $eventGeo"
}