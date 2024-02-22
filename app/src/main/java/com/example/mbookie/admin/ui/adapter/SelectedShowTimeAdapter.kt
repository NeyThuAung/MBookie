package com.example.mbookie.admin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.data.model.ShowDate
import com.example.mbookie.data.model.ShowTime
import com.example.mbookie.databinding.ShowDateItemCardBinding
import com.example.mbookie.databinding.ShowTimeItemCardBinding


class SelectedShowTimeAdapter(
    private var showTimeList: ArrayList<ShowTime>,
    private val showDatePos : Int,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SelectedShowTimeAdapter.SelectedShowTimeHolder>() {


    interface OnItemClickListener {
        fun onRemoveShowTimeClick(showDatePos : Int, showTimePos : Int)
    }

    inner class SelectedShowTimeHolder(private val binding: ShowTimeItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(showTime: ShowTime) {

            binding.tvSelectedTime.text = showTime.time

            binding.ivDeleteTime.setOnClickListener {
                listener.onRemoveShowTimeClick(showDatePos, adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedShowTimeHolder {
        return SelectedShowTimeHolder(
            ShowTimeItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectedShowTimeHolder, position: Int) {
        holder.bind(showTimeList[position])
    }

    override fun getItemCount(): Int {
        return showTimeList.size
    }

}