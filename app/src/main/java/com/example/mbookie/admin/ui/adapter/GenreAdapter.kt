package com.example.mbookie.admin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.data.model.Genre
import com.example.mbookie.databinding.SelectGenreItemCardBinding

class GenreAdapter(
    private var genreList : ArrayList<Genre>,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<GenreAdapter.GenreHolder>() {

    interface OnItemClickListener {
        fun onEditClick(genreInfo: Genre)
        fun onDeleteClick(genreId : String,deletePos : Int)
        fun onCheckCategoryState(checkedGenre: Genre)
        fun onUncheckCategoryState(genreId : String)
    }

    inner class GenreHolder(private val binding : SelectGenreItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(genre : Genre){
            binding.tvGenre.text = genre.genre

            binding.cbSelectGenre.isChecked = genre.isSelected

            binding.root.setOnClickListener {
                binding.cbSelectGenre.isChecked = !binding.cbSelectGenre.isChecked
                if (binding.cbSelectGenre.isChecked) {
                    genre.isSelected = true
                    listener.onCheckCategoryState(genre)
                } else {
                    genre.isSelected = false
                    genre.id?.let { it1 -> listener.onUncheckCategoryState(it1) }
                }
            }

            binding.cbSelectGenre.setOnClickListener {
                if (binding.cbSelectGenre.isChecked){
                    genre.isSelected = true
                    listener.onCheckCategoryState(genre)
                }else {
                    genre.isSelected = false
                    genre.id?.let { it1 -> listener.onUncheckCategoryState(it1) }
                }
            }

            binding.ivEdit.setOnClickListener {
                listener.onEditClick(genre)
            }

            binding.ivDelete.setOnClickListener {
                listener.onDeleteClick(genre.id.toString(),adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        return GenreHolder(SelectGenreItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.bind(genreList[position])
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

}