package com.example.mbookie.admin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.data.model.Show
import com.example.mbookie.databinding.AddedShowtimeItemBinding

class ShowtimeAdapter(
    private var showList: ArrayList<Show>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ShowtimeAdapter.ShowtimeHolder>() {


    interface OnItemClickListener {
        fun onRemoveShowClick(showId : String,removePos : Int)
    }

    inner class ShowtimeHolder(private val binding: AddedShowtimeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(show: Show) {

            binding.tvShowDate.text = show.showdate
            binding.tvShowTime.text = show.showtime

            binding.ivDeleteShow.setOnClickListener {
                listener.onRemoveShowClick(show.sid.toString(),adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowtimeHolder {
        return ShowtimeHolder(
            AddedShowtimeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowtimeHolder, position: Int) {
        holder.bind(showList[position])
    }

    override fun getItemCount(): Int {
        return showList.size
    }

}