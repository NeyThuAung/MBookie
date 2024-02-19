package com.example.mbookie.data.model

data class Cinema(
    var cId : String ?="",
    val cName : String ?="",
    val cTotalSeat : Int ?=0,
    val cAddress : String ?=""
)

data class Seat(
    var sId : String ?="",
    val seatNumber : String ?="",
    val seatAvailableStatus : Boolean ?= false,
    val cinemaId : String ?=""
)