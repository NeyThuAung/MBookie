package com.example.mbookie.data.repository

import android.net.Uri
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.util.UiState

interface MovieRepository {
    fun saveGenre(genre : Genre, result : (UiState<String>) -> Unit)
    fun getGenreList(result : (UiState<List<Genre>>) -> Unit)
    fun updateGenre(genre : Genre, result : (UiState<String>) -> Unit)
    fun deleteGenre(genreId : String, result : (UiState<String>) -> Unit)
    fun saveMovie(movie : MovieDetail, result : (UiState<String>) -> Unit)

}