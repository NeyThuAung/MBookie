package com.example.mbookie.data.model

data class Genre(
    var id : String ?="",
    var genre : String ?="",
    var isSelected : Boolean = false,
)

data class MovieDetail(
    var mId : String ?="",
    var mPosterImg : String ?="",
    var mTitle : String ?="",
    var mCategoryId : Int ?=0,
    var mGenreIdList : ArrayList<String> ?= arrayListOf(),
    var mGenre : String ?="",
    var mDuration : String ?="",
    var mReleaseDate : String ?="",
    var mCensorship : String ?="",
    var mTrailerLink : String ?="",
    var mDescription : String ?="",
    var mLanguage : String ?=""
)

data class MovieShowCinema(
    var id: String ?="",
    var movieId : String ?="",
    var showId : String ?="",
    var cinema : String ?=""
)
