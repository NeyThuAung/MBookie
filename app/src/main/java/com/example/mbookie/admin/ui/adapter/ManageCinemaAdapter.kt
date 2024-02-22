package com.example.mbookie.admin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.R
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.databinding.CinemaItemCardBinding
import com.example.mbookie.databinding.ManageCinemaItemCardBinding


class ManageCinemaAdapter(
    private var cinemaList : ArrayList<Cinema>,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<ManageCinemaAdapter.ManageCinemaHolder>() {


    interface OnItemClickListener {
        fun onEditCinemaClick(cinema : Cinema)
        fun onDeleteCinemaClick(cinemaId : String, deletePos : Int)
    }

    inner class ManageCinemaHolder(private val binding : ManageCinemaItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(cinemaDetail : Cinema){

            binding.tvCinemaName.text = cinemaDetail.name
            binding.tvLocation.text = cinemaDetail.address

            binding.ivEdit.setOnClickListener {
                listener.onEditCinemaClick(cinemaDetail)
            }

            binding.ivDelete.setOnClickListener {
                listener.onDeleteCinemaClick(cinemaDetail.id.toString(),adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageCinemaHolder {
        return ManageCinemaHolder(ManageCinemaItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ManageCinemaHolder, position: Int) {
        holder.bind(cinemaList[position])
    }

    override fun getItemCount(): Int {
        return cinemaList.size
    }

}