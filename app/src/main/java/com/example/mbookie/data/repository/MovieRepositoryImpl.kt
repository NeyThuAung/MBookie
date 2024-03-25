package com.example.mbookie.data.repository

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.data.model.Seat
import com.example.mbookie.data.model.Show
import com.example.mbookie.data.model.ShowDate
import com.example.mbookie.data.model.ShowMovieCinema
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
                    UiState.Success(document.id)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage as String
                    )
                )
            }
    }

    override fun saveCinema(cinema: Cinema, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTables.CINEMA).document()
        cinema.id = document.id

        document
            .set(cinema)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(document.id)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage as String
                    )
                )
            }
    }

    override fun saveSeat(seatList: ArrayList<Seat>, result: (UiState<String>) -> Unit) {
        val seatCollectionRef = database.collection(FireStoreTables.SEAT)

        val batch = database.batch()

        seatList.forEach { seat ->
            val seatData = Seat(
                sId = "",
                seatNumber = seat.seatNumber,
                seatAvailableStatus = seat.seatAvailableStatus,
                cinemaId = seat.cinemaId
            )

            val docRef = seatCollectionRef.document()
            seatData.sId = docRef.id

            batch.set(docRef, seatData)

        }

        batch.commit()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Seats has been successfully created.")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage as String
                    )
                )
            }
    }

    override fun getMovieList(result: (UiState<List<MovieDetail>>) -> Unit) {
        database.collection(FireStoreTables.MOVIE)
            .get()
            .addOnSuccessListener {
                val movieList = arrayListOf<MovieDetail>()
                for (document in it) {
                    val movie = document.toObject(MovieDetail::class.java)
                    movieList.add(movie)

                }
                result.invoke(
                    UiState.Success(movieList)
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

    override fun getCinemaList(result: (UiState<List<Cinema>>) -> Unit) {
        database.collection(FireStoreTables.CINEMA)
            .get()
            .addOnSuccessListener {
                val cinemaList = arrayListOf<Cinema>()
                for (document in it) {
                    val cinema = document.toObject(Cinema::class.java)
                    cinemaList.add(cinema)

                }
                result.invoke(
                    UiState.Success(cinemaList)
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

    override fun saveShowTime(
        showDateList: ArrayList<ShowDate>,
        mId: String,
        cId: String,
        result: (UiState<String>) -> Unit
    ) {
        val showtimeCollectionRef = database.collection(FireStoreTables.SHOWTIME)
        val smcCollectionRef = database.collection(FireStoreTables.SHOW_MOVIE_CINEMA)

        val batch = database.batch()

        showDateList.forEach { showDate ->

            showDate.showTimeList.forEach { showTime ->
                val show = Show(
                    sid = "",
                    showdate = showDate.date,
                    showtime = showTime.time
                )
                val docRef = showtimeCollectionRef.document()
                show.sid = docRef.id
                batch.set(docRef, show)

                val smcData = ShowMovieCinema(
                    id = "",
                    showId = docRef.id,
                    movieId = mId,
                    cinemaId = cId
                )
                val smcDocRef = smcCollectionRef.document()
                smcData.id = smcDocRef.id
                batch.set(smcDocRef, smcData)
            }

        }

        batch.commit()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Showtime has been successfully created.")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage as String
                    )
                )
            }
    }

    override fun getShowList(
        movieId: String,
        cinemaId: String,
        result: (UiState<List<Show>>) -> Unit
    ) {

        database.collection(FireStoreTables.SHOW_MOVIE_CINEMA)
            .whereEqualTo("movieId", movieId)
            .whereEqualTo("cinemaId", cinemaId)
            .get()
            .addOnSuccessListener {

                val showIdList = arrayListOf<String>()

                if (!it.isEmpty) {
                    for (document in it) {
                        val showMovieCinema = document.toObject(ShowMovieCinema::class.java)
                        showIdList.add(showMovieCinema.showId.toString())
                    }

                    if (showIdList.isNotEmpty()) {
                        val showList: ArrayList<Show> = arrayListOf()

                        database.collection(FireStoreTables.SHOWTIME).whereIn("sid", showIdList)
                            .get()
                            .addOnSuccessListener {
                                for (document in it) {
                                    val show = document.toObject(Show::class.java)
                                    showList.add(show)
                                }
                                result.invoke(
                                    UiState.Success(showList)
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
                } else {
                    result.invoke(
                        UiState.Success(arrayListOf<Show>())
                    )
                }

            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }
    }

    override fun deleteShow(showId: String, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTables.SHOWTIME).document(showId)
        document
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Showtime has been successfully deleted.")
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

    override fun updateMovieDetail(movieDetail: MovieDetail, result: (UiState<String>) -> Unit) {
        val document =
            database.collection(FireStoreTables.MOVIE).document(movieDetail.mId.toString())
        document
            .set(movieDetail)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Movie details has been successfully updated.")
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

    override fun getAvailableCinemaForMovieList(
        movieId: String,
        result: (UiState<List<Cinema>>) -> Unit
    ) {
        database.collection(FireStoreTables.SHOW_MOVIE_CINEMA)
            .whereEqualTo("movieId", movieId)
            .get()
            .addOnSuccessListener {

                val cinemaIdList = arrayListOf<String>()

                if (!it.isEmpty) {
                    for (document in it) {
                        val showMovieCinema = document.toObject(ShowMovieCinema::class.java)
                        cinemaIdList.add(showMovieCinema.cinemaId.toString())
                    }

                    if (cinemaIdList.isNotEmpty()) {
                        val cinemaList: ArrayList<Cinema> = arrayListOf()

                        database.collection(FireStoreTables.CINEMA).whereIn("id", cinemaIdList)
                            .get()
                            .addOnSuccessListener {
                                for (document in it) {
                                    val cinema = document.toObject(Cinema::class.java)
                                    cinemaList.add(cinema)
                                }
                                result.invoke(
                                    UiState.Success(cinemaList)
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
                } else {
                    result.invoke(
                        UiState.Success(arrayListOf<Cinema>())
                    )
                }

            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }
    }

    override fun updateCinema(cinema: Cinema, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTables.CINEMA).document(cinema.id.toString())
        document
            .set(cinema)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Cinema has been successfully updated.")
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

    override fun deleteSeat(cinemaId: String, result: (UiState<String>) -> Unit) {
        val collectionRef = database.collection(FireStoreTables.SEAT)
        val query = collectionRef.whereEqualTo("cinemaId", cinemaId)

        query
            .get()
            .addOnSuccessListener { querySnapshot ->
                val batch = database.batch()

                querySnapshot.documents.forEach { document ->
                    batch.delete(document.reference)
                }

                batch.commit().addOnSuccessListener {
                    result.invoke(
                        UiState.Success("Seats with cinemaId $cinemaId have been successfully deleted.")
                    )
                }.addOnFailureListener { exception ->
                    result.invoke(
                        UiState.Failure(exception.localizedMessage)
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }

    }

    override fun deleteCinema(cinemaId: String, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreTables.SHOW_MOVIE_CINEMA)
            .whereEqualTo("cinemaId", cinemaId)
            .get()
            .addOnSuccessListener { smcQuerySnapshot ->
                val batch = database.batch()
                val showIdList = arrayListOf<String>()

                if (!smcQuerySnapshot.isEmpty) {
                    for (document in smcQuerySnapshot) {
                        val showMovieCinema = document.toObject(ShowMovieCinema::class.java)
                        showIdList.add(showMovieCinema.showId.toString())
                        batch.delete(document.reference)
                    }

                    if (showIdList.isNotEmpty()) {
                        val showCollectionRef = database.collection(FireStoreTables.SHOWTIME)
                        val showQuery = showCollectionRef.whereIn("sid", showIdList)
                        showQuery
                            .get()
                            .addOnSuccessListener { showQuerySnapShot ->

                                showQuerySnapShot.documents.forEach { document ->
                                    batch.delete(document.reference)
                                }
                                batch.commit()
                                    .addOnSuccessListener {

                                    }.addOnFailureListener { exception ->
                                        result.invoke(
                                            UiState.Failure(exception.localizedMessage)
                                        )
                                    }

                            }
                    }

                }

                val document = database.collection(FireStoreTables.CINEMA).document(cinemaId)
                document
                    .delete()
                    .addOnSuccessListener {
                        deleteSeat(cinemaId,result)
                        result.invoke(
                            UiState.Success("Cinema '$cinemaId' has been successfully deleted.")
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
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }
    }

    override fun deleteMovie(movieId: String, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreTables.SHOW_MOVIE_CINEMA)
            .whereEqualTo("movieId", movieId)
            .get()
            .addOnSuccessListener { smcQuerySnapshot ->
                val batch = database.batch()
                val showIdList = arrayListOf<String>()

                if (!smcQuerySnapshot.isEmpty) {

                    smcQuerySnapshot.documents.forEach { smcDocument ->
                        batch.delete(smcDocument.reference) // Delete from SHOW_MOVIE_CINEMA
                        val showMovieCinema = smcDocument.toObject(ShowMovieCinema::class.java)
                        showIdList.add(showMovieCinema?.showId.toString())
                    }


                    if (showIdList.isNotEmpty()) {
                        val showCollectionRef = database.collection(FireStoreTables.SHOWTIME)
                        val showQuery = showCollectionRef.whereIn("sid", showIdList)
                        showQuery
                            .get()
                            .addOnSuccessListener { showQuerySnapShot ->

                                showQuerySnapShot.documents.forEach { document ->
                                    batch.delete(document.reference)
                                }

                                batch.commit()
                                    .addOnSuccessListener {
                                        deleteMovieDocument(movieId, result)
                                    }
                                    .addOnFailureListener { exception ->
                                        result.invoke(
                                            UiState.Failure(exception.localizedMessage)
                                        )
                                    }
                            }
                    } else {
                        // No related documents found in SHOWTIME collection
                        batch.commit()
                            .addOnSuccessListener {
                                deleteMovieDocument(movieId, result)
                            }
                            .addOnFailureListener { exception ->
                                result.invoke(
                                    UiState.Failure(exception.localizedMessage)
                                )
                            }
                    }

                } else {
                    // No related documents found in SHOW_MOVIE_CINEMA collection
                    deleteMovieDocument(movieId, result)
                }

            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage!!
                    )
                )
            }
    }

    override fun getGenreListWithIdList(
        selectedGenreIdLst: ArrayList<String>,
        result: (UiState<List<Genre>>) -> Unit
    ) {
        database.collection(FireStoreTables.GENRE).whereIn("id",selectedGenreIdLst)
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

    override fun getMovieListWithCategory(
        mCategory: Int,
        result: (UiState<List<MovieDetail>>) -> Unit
    ) {
        database.collection(FireStoreTables.MOVIE)
            .whereEqualTo("mcategoryId",mCategory)
            .get()
            .addOnSuccessListener {
                val movieList = arrayListOf<MovieDetail>()
                for (document in it) {
                    val movie = document.toObject(MovieDetail::class.java)
                    movieList.add(movie)
                }
                result.invoke(
                    UiState.Success(movieList)
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

    private fun deleteMovieDocument(movieId: String, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreTables.MOVIE).document(movieId)
        document
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Movie '$movieId' has been successfully deleted.")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage ?: "Failed to delete cinema.")
                )
            }
    }

}
