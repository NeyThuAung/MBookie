package com.example.mbookie.data.repository

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.util.FireStoreTables
import com.example.mbookie.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class MovieRepositoryImpl(
    private val database: FirebaseFirestore
) : MovieRepository {

    override fun saveGenre(genre: Genre, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTables.GENRE).document()
        genre.id = document.id
        document
            .set(genre)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Genre has been successfully created.")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }
    }

    override fun getGenreList(result: (UiState<List<Genre>>) -> Unit) {
        database.collection(FireStoreTables.GENRE)
            .get()
            .addOnSuccessListener {
                val genreList = arrayListOf<Genre>()
                for (document in it) {
                    val genre = document.toObject(Genre::class.java)
                    genreList.add(genre)

                }
                result.invoke(
                    UiState.Success(genreList)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }
    }

    override fun updateGenre(genre: Genre, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTables.GENRE).document(genre.id.toString())
        document
            .set(genre)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Genre has been successfully updated.")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }
    }

    override fun deleteGenre(genreId: String, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTables.GENRE).document(genreId)
        document
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Genre has been successfully deleted.")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }
    }

    override fun saveMovie(movie: MovieDetail, result: (UiState<String>) -> Unit) {

        val document = database.collection(FireStoreTables.MOVIE).document()
        movie.mId = document.id

        document
            .set(movie)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Movie has been successfully created.")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

}
