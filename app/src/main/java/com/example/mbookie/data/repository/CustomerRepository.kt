package com.example.mbookie.data.repository

import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.util.AppSharedPreference
import com.example.mbookie.util.UiState
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    fun getMovieListForCustomer() : Flow<UiState<List<MovieDetail>>>

    fun getFirstMovieList(mCategory : String) : Flow<UiState<List<MovieDetail>>>

    fun getMovieListWithPagination(share : AppSharedPreference,mCategory : String) : Flow<UiState<List<MovieDetail>>>
}