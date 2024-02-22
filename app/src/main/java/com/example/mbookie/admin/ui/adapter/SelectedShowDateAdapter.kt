package com.example.mbookie.admin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.data.model.ShowDate
import com.example.mbookie.databinding.ShowDateItemCardBinding


class SelectedShowDateAdapter(
    private var showDateList: ArrayList<ShowDate>,
    private val listener: OnItemClickListener,
    private val showTimeListener : SelectedShowTimeAdapter.OnItemClickListener
) : RecyclerView.Adapter<SelectedShowDateAdapter.SelectedShowDateHolder>() {


    interface OnItemClickListener {
        fun onRemoveSelectedDateClick(pos : Int)
        fun onAddShowTimeClick(pos: Int)
    }

    inner class SelectedShowDateHolder(private val binding: ShowDateItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(showDate: ShowDate) {

            binding.tvShowDate.text = showDate.date

            if (!showDate.showTimeList.isNullOrEmpty()) {
                binding.recSelectedShowTime.isVisible = true
                binding.recSelectedShowTime.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(itemView.context,LinearLayoutManager.HORIZONTAL,false)
                    adapter = SelectedShowTimeAdapter(showDate.showTimeList ?: arrayListOf() ,adapterPosition,showTimeListener)
                }
            }else{
                binding.recSelectedShowTime.isVisible = false
            }

            binding.ivDeleteDate.setOnClickListener {
                listener.onRemoveSelectedDateClick(adapterPosition)
            }
            binding.cvAddShowTime.setOnClickListener {
                listener.onAddShowTimeClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedShowDateHolder {
        return SelectedShowDateHolder(
            ShowDateItemCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectedShowDateHolder, position: Int) {
        holder.bind(showDateList[position])
    }

    override fun getItemCount(): Int {
        return showDateList.size
    }

}