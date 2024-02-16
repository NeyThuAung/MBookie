package com.example.mbookie.data.repository

import com.example.mbookie.data.model.Genre
import com.example.mbookie.util.UiState

interface MovieRepository {

    fun saveGenre(genre : Genre, result : (UiState<String>) -> Unit)

    fun updateGenre(genre : Genre, result : (UiState<String>) -> Unit)

    fun getGenreList(result : (UiState<List<Genre>>) -> Unit)

    fun deleteGenre(genreId : String, result : (UiState<String>) -> Unit)

}