package com.example.mbookie.util

import android.media.Image
import com.example.mbookie.R

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

data class ImageSlider(
    val id: Int,
    val movieId : String,
    val image : String,
    val title : String
)

val image1 = ImageSlider(1, "1","https://firebasestorage.googleapis.com/v0/b/mbookie-675a6.appspot.com/o/Images%2F1709304844964?alt=media&token=0abbcd30-9aa3-43a8-863f-fc88efe2cec0","First")
val image2 = ImageSlider(2,"1", "https://firebasestorage.googleapis.com/v0/b/mbookie-675a6.appspot.com/o/Images%2F1709304844964?alt=media&token=0abbcd30-9aa3-43a8-863f-fc88efe2cec0","Second")
val image3 = ImageSlider(3,"1", "https://firebasestorage.googleapis.com/v0/b/mbookie-675a6.appspot.com/o/Images%2F1711207540160?alt=media&token=d51baa8a-4e08-41de-8642-74c401dece4c","Third")
val image4 = ImageSlider(4,"1", "https://firebasestorage.googleapis.com/v0/b/mbookie-675a6.appspot.com/o/Images%2F1709304844964?alt=media&token=0abbcd30-9aa3-43a8-863f-fc88efe2cec0","Fourth")

val imageList = arrayListOf<ImageSlider>(
    image1, image2, image3, image4
)
