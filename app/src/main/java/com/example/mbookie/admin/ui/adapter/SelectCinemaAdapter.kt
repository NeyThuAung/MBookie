package com.example.mbookie.admin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mbookie.R
import com.example.mbookie.data.model.Cinema
import com.example.mbookie.databinding.CinemaItemCardBinding


class SelectCinemaAdapter(
    private var cinemaList : ArrayList<Cinema>,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<SelectCinemaAdapter.SelectCinemaHolder>() {

    var selectedPos = -1

    interface OnItemClickListener {
        fun onMovieRootClick(cinema : Cinema)
    }

    inner class SelectCinemaHolder(private val binding : CinemaItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("NotifyDataSetChanged")
        fun bind(cinemaDetail : Cinema){

            binding.tvCinemaName.text = cinemaDetail.name
            binding.tvLocation.text = cinemaDetail.address

            binding.root.setOnClickListener {

                selectedPos = adapterPosition
                listener.onMovieRootClick(cinemaDetail)
                notifyDataSetChanged()

            }

            if (selectedPos == adapterPosition){

                binding.cvCinema.strokeWidth =
                    itemView.resources.getDimensionPixelSize(R.dimen.cardView_stroke_width)
                binding.cvCinema.strokeColor =
                    itemView.resources.getColor(R.color.green)

            }else{
                binding.cvCinema.strokeWidth = 0
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCinemaHolder {
        return SelectCinemaHolder(CinemaItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SelectCinemaHolder, position: Int) {
        holder.bind(cinemaList[position])
    }

    override fun getItemCount(): Int {
        return cinemaList.size
    }

}