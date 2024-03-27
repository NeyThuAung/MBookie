package com.example.mbookie.data.usecase

import android.graphics.Movie
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.repository.CustomerRepository
import com.example.mbookie.util.AppSharedPreference
import com.example.mbookie.util.UiState
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import javax.inject.Inject

class CustomerUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {

    fun getMovieListForCustomer() : kotlinx.coroutines.flow.Flow<UiState<List<MovieDetail>>> {
        return customerRepository.getMovieListForCustomer()
    }

    fun getFirstMovieList(mCategory : String) : kotlinx.coroutines.flow.Flow<UiState<List<MovieDetail>>> {
        return customerRepository.getFirstMovieList(mCategory)
    }

    fun getMovieListWithPagination(share : AppSharedPreference,mCategory : String) : kotlinx.coroutines.flow.Flow<UiState<List<MovieDetail>>> {
        return customerRepository.getMovieListWithPagination(share,mCategory)
    }

    fun getMovieDetail(movieId : String) : kotlinx.coroutines.flow.Flow<UiState<MovieDetail>> {
        return customerRepository.getMovieDetail(movieId)
    }

    fun getAvailableCinemaForMovie(movieId : String) : kotlinx.coroutines.flow.Flow<UiState<List<Cinema>>> {
        return customerRepository.getAvailableCinemaForMovie(movieId)
    }
}