package com.example.mbookie.admin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mbookie.data.model.Genre
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.databinding.MovieListItemCardBinding
import com.example.mbookie.databinding.SelectGenreItemCardBinding


class MovieListAdapter(
    private var movieList : ArrayList<MovieDetail>,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<MovieListAdapter.MovieListHolder>() {

    interface OnItemClickListener {
        fun onEditMovieClick(movieDetail: MovieDetail)
        fun onDeleteMovieClick(movieName : String, movieId : String, removePos : Int)
    }

    inner class MovieListHolder(private val binding : MovieListItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(movie : MovieDetail){

            Glide.with(itemView)
                .load(movie.mPosterImg)
                .into(binding.ivMovieImage)

            binding.tvMovieTitle.text = movie.mTitle
            binding.tvGenre.text = movie.mGenre
            binding.tvDuration.text = movie.mDuration
            binding.tvReleaseDate.text = movie.mReleaseDate

            binding.ivEdit.setOnClickListener {
                listener.onEditMovieClick(movie)
            }

            binding.ivDelete.setOnClickListener {
                listener.onDeleteMovieClick(movie.mTitle.toString(), movie.mId.toString(),adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListHolder {
        return MovieListHolder(MovieListItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MovieListHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}