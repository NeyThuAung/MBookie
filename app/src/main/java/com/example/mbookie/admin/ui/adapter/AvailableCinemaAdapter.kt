package com.example.mbookie.admin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.databinding.SelectedCinemaItemCardBinding

class AvailableCinemaAdapter(
    private var cinemaList : ArrayList<Cinema>,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<AvailableCinemaAdapter.AvailableCinemaHolder>() {

    var selectedPos = -1

    interface OnItemClickListener {
        fun onMovieRootClick(cinema : Cinema)
    }

    inner class AvailableCinemaHolder(private val binding : SelectedCinemaItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(cinemaDetail : Cinema){

            binding.tvCinemaName.text = cinemaDetail.name
            binding.tvLocation.text = cinemaDetail.address

            binding.root.setOnClickListener {

                listener.onMovieRootClick(cinemaDetail)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableCinemaHolder {
        return AvailableCinemaHolder(SelectedCinemaItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AvailableCinemaHolder, position: Int) {
        holder.bind(cinemaList[position])
    }

    override fun getItemCount(): Int {
        return cinemaList.size
    }

}