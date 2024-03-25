package com.example.mbookie.customer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.databinding.CustomerMovieItemCardBinding

class CustomerMovieListAdapter(
    private var movieList: ArrayList<MovieDetail>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CustomerMovieListAdapter.CustomerMovieListHolder>() {

    interface OnItemClickListener {
        fun onMovieDetailClick(movie: MovieDetail)
    }

    inner class CustomerMovieListHolder(private val binding: CustomerMovieItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")

        fun bind(movie: MovieDetail) {

            Glide.with(itemView)
                .load(movie.mPosterImg)
                .centerCrop()
                .into(binding.ivMoviePoster)

            binding.tvMovieTitle.text = movie.mTitle
            binding.tvGenre.text = movie.mGenre
            binding.tvReleaseDate.text = movie.mReleaseDate

            binding.root.setOnClickListener {
                listener.onMovieDetailClick(movie)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerMovieListHolder {
        return CustomerMovieListHolder(
            CustomerMovieItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomerMovieListHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {

        return if (movieList.size >= 10){
            10
        }else
            movieList.size
    }

}