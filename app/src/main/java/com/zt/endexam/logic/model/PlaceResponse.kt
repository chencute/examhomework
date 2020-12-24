package com.zt.endexam.logic.model


data class PlaceResponse(val code:String,val locations:List<Location>)

data class Location(val name:String,val adm2:String,val adm1:String,val country:String ,val lon:String,val lat:String
,val rank:String)