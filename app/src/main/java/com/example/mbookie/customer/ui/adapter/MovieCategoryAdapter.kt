package com.example.mbookie.customer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.data.model.MovieListForCustomer
import com.example.mbookie.databinding.CustomerMovieCategoryItemCardBinding

class MovieCategoryAdapter(
    private var categoryMovieList: ArrayList<MovieListForCustomer>,
    private val listener: OnItemClickListener,
    private val customerMovieListener: CustomerMovieListAdapter.OnItemClickListener
) : RecyclerView.Adapter<MovieCategoryAdapter.MovieCategoryHolder>() {


    interface OnItemClickListener {
        fun onSeeAllClick(movieCategory : String)
    }

    inner class MovieCategoryHolder(private val binding: CustomerMovieCategoryItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(movieCategory: MovieListForCustomer) {


            binding.tvMovieCategory.text = movieCategory.mCategory

            if (!movieCategory.movieList.isNullOrEmpty()) {
                binding.recMovieList.isVisible = true
                binding.recMovieList.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(
                        itemView.context,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                    adapter = CustomerMovieListAdapter(
                        movieCategory.movieList ?: arrayListOf(),
                        customerMovieListener
                    )
                }
            } else {
                binding.recMovieList.isVisible = false
            }

            binding.llSeeAllNowPlaying.setOnClickListener {
                listener.onSeeAllClick(movieCategory.mCategory.toString())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCategoryHolder {
        return MovieCategoryHolder(
            CustomerMovieCategoryItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieCategoryHolder, position: Int) {
        holder.bind(categoryMovieList[position])
    }

    override fun getItemCount(): Int {
        return categoryMovieList.size
    }

}