package com.example.mbookie.util

data class MovieCategoryData(
    val id: Int,
    val movieCategoryName: String
)

val movieCategory1 = MovieCategoryData(-1, "Please Select One")
val movieCategory2 = MovieCategoryData(1, "Popular")
val movieCategory3 = MovieCategoryData(2, "Upcoming")
val movieCategory4 = MovieCategoryData(3, "Now playing")

val movieCategoryList = arrayListOf<MovieCategoryData>(
    movieCategory1, movieCategory2, movieCategory3,
    movieCategory4
)

data class TotalSeatData(
    val id: Int,
    val totalSeat : String
)

val totalSeat = TotalSeatData(-1,"Please Select One")
val totalSeat1 = TotalSeatData(1,"30")
val totalSeat2 = TotalSeatData(2,"40")
val totalSeat3 = TotalSeatData(3,"50")
val totalSeat4 = TotalSeatData(4,"60")

val totalSeatList = arrayListOf<TotalSeatData>(
    totalSeat,totalSeat1, totalSeat2, totalSeat3,
    totalSeat4
)
