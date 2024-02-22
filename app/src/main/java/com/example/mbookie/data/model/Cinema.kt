package com.example.mbookie.data.model

data class Cinema(
    var id : String ?="",
    val name : String ?="",
    val totalSeat : Int ?=0,
    val address : String ?=""
)

data class Seat(
    var sId : String ?="",
    val seatNumber : String ?="",
    val seatAvailableStatus : Boolean ?= false,
    val cinemaId : String ?=""
)