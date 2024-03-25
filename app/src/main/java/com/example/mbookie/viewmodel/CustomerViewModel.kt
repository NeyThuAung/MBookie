package com.example.mbookie.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.usecase.CustomerUseCase
import com.example.mbookie.util.AppSharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerUseCase: CustomerUseCase
): ViewModel() {

    var movieList : ArrayList<MovieDetail> = arrayListOf()
    var mCategory : String = ""

    fun getMovieListForCustomer() = customerUseCase.getMovieListForCustomer()

    fun getFirstMovieList(mCategory : String) = customerUseCase.getFirstMovieList(mCategory)

    fun getMovieListWithPagination(share : AppSharedPreference,mCategory : String) = customerUseCase.getMovieListWithPagination(share,mCategory)
}