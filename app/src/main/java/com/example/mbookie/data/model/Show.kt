package com.example.mbookie.data.model

data class Show(
    var sid : String ?="",
    var showdate : String ?="",
    var showtime : String ?="",
)

data class ShowMovieCinema(
    var id : String ?="",
    var showId : String ?="",
    var movieId : String ?="",
    var cinemaId : String ?="",
)

data class ShowDate(
    var id : Int ?=0,
    var date : String ?="",
    var showTimeList : ArrayList<ShowTime> = arrayListOf()
)

data class ShowTime(
    var id : Int ?=0,
    var time : String ?="",
)