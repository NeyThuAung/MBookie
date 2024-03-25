package com.example.mbookie.data.repository

import android.util.Log
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.util.AppSharedPreference
import com.example.mbookie.util.FireStoreTables
import com.example.mbookie.util.UiState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class CustomerRepositoryImpl(
    private val database: FirebaseFirestore
) : CustomerRepository {

    override fun getMovieListForCustomer(): Flow<UiState<List<MovieDetail>>> =
        flow {
            emit(UiState.Loading)
            try {
                val querySnapshot = database.collection(FireStoreTables.MOVIE)
                    .get()
                    .await()

                val movieList = ArrayList<MovieDetail>()
                for (document in querySnapshot) {
                    val movie = document.toObject(MovieDetail::class.java)
                    movieList.add(movie)
                }

                emit(UiState.Success(movieList))

            } catch (exception: Exception) {
                emit(UiState.Failure(exception.localizedMessage ?: "Unknown error"))
            }
        }.catch { exception ->
            emit(UiState.Failure(exception.localizedMessage ?: "Unknown error"))
        }.flowOn(Dispatchers.IO)


    private lateinit var lastVisible: DocumentSnapshot

    //First Movie Movie
    override fun getFirstMovieList(mCategory : String): Flow<UiState<List<MovieDetail>>> = flow {
        emit(UiState.Loading)
        try {
            val querySnapshot = database.collection(FireStoreTables.MOVIE)
                .whereEqualTo("mcategoryId",mCategory)
                .limit(6)
                .get()
                .await()

            val movieList = ArrayList<MovieDetail>()
            for (document in querySnapshot) {
                val movie = document.toObject(MovieDetail::class.java)
                movieList.add(movie)
            }
            lastVisible = querySnapshot.documents[querySnapshot.size() - 1]
            Log.d("JKLHJKHK", "getMovieListForCustomer: $lastVisible")
            emit(UiState.Success(movieList))

        } catch (exception: Exception) {
            emit(UiState.Failure(exception.localizedMessage ?: "Unknown error"))
        }
    }.catch { exception ->
        emit(UiState.Failure(exception.localizedMessage ?: "Unknown error"))
    }.flowOn(Dispatchers.IO)

    override fun getMovieListWithPagination(share: AppSharedPreference,mCategory : String): Flow<UiState<List<MovieDetail>>> = flow {
        emit(UiState.Loading)
        try {
            val querySnapshot = database.collection(FireStoreTables.MOVIE)
                .whereEqualTo("mcategoryId",mCategory)
                .limit(6)
                .startAfter(lastVisible)
                .get()
                .await()

            val movieList = ArrayList<MovieDetail>()
            for (document in querySnapshot) {
                val movie = document.toObject(MovieDetail::class.java)
                movieList.add(movie)
            }
            lastVisible = querySnapshot.documents[querySnapshot.size() - 1]
            Log.d("JKLHJKHK", "getMovieListForCustomer: $lastVisible")

            if (querySnapshot.size() < 10){
                share.save("isLastItemReached",true)
            }

            emit(UiState.Success(movieList))

        } catch (exception: Exception) {
            emit(UiState.Failure(exception.localizedMessage ?: "Unknown error"))
        }
    }.catch { exception ->
        emit(UiState.Failure(exception.localizedMessage ?: "Unknown error"))
    }.flowOn(Dispatchers.IO).flowOn(Dispatchers.IO)


}
