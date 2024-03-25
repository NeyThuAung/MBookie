package com.example.mbookie.customer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mbookie.R
import com.example.mbookie.databinding.ImageSliderItemCardBinding
import com.example.mbookie.util.ImageSlider
import com.example.mbookie.util.image1

class ImageSliderAdapter(
    private val viewPager : ViewPager2,
    private val imageList : ArrayList<ImageSlider>,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderHolder>() {

    interface OnItemClickListener{
        fun onMoviePosterClick(movieId : String)
    }

    inner class ImageSliderHolder(private val binding : ImageSliderItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(image : ImageSlider){

            Glide.with(binding.root)
                .load(image.image)
                .placeholder(R.drawable.image1)
                .into(binding.ivImageSlider)

            binding.root.setOnClickListener{
                listener.onMoviePosterClick(image.movieId)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderHolder {
        return ImageSliderHolder(ImageSliderItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageSliderHolder, position: Int) {
        holder.bind(imageList[position])
        if (position == imageList.size-1){
            viewPager.post(run)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    val run = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}