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