package com.example.mbookie.customer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mbookie.data.model.MovieDetail
import com.example.mbookie.databinding.SeeAllMovieItemCardBinding


class SeeAllMovieAdapter(
    private val listener: OnOrderClickListener
) : androidx.recyclerview.widget.ListAdapter<MovieDetail, SeeAllMovieAdapter.SeeAllMovieHolder>(Diff) {

    interface OnOrderClickListener {
        fun onRootClick(movieId: String)
    }

    inner class SeeAllMovieHolder(private val binding: SeeAllMovieItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movie: MovieDetail) {
            Glide.with(itemView)
                .load(movie.mPosterImg)
                .centerCrop()
                .into(binding.ivMoviePoster)

            binding.tvMovieTitle.text = movie.mTitle
            binding.tvGenre.text = movie.mGenre
            binding.tvReleaseDate.text = movie.mReleaseDate

            binding.root.setOnClickListener {
                listener.onRootClick(movie.mId.toString())
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<MovieDetail>() {
        override fun areItemsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean =
            oldItem.mId == newItem.mId

        override fun areContentsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean =
            oldItem == newItem
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeAllMovieHolder {
        return SeeAllMovieHolder(
            SeeAllMovieItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SeeAllMovieHolder, position: Int) {
        val order = getItem(position)
        if (order != null) {
            holder.bind(order)
        }
    }

}
