package com.example.mbookie.util

import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.example.mbookie.R

class LoadingDialog (var activity: FragmentActivity) {

    var dialog: Dialog? = null

    fun showDialog() {
        if(dialog != null){
            if(dialog!!.isShowing){
                dialog!!.dismiss()
            }
        }else {
            dialog = Dialog(activity)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //dialog?.setCancelable(false)
            dialog?.setContentView(R.layout.dialog_loading)

            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog!!.window?.attributes)

            dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_margin)
            //dialog!!.window?.attributes = lp

            val gifImageView = dialog?.findViewById<ImageView>(R.id.iv_loading)
            val imageViewTarget = DrawableImageViewTarget(gifImageView)
            val tvLoading = dialog?.findViewById<TextView>(R.id.tv_loading)

            //   tvLoading?.text = tvText

            tvLoading?.setOnClickListener {
                dialog?.dismiss()
            }

            Glide.with(activity)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageViewTarget)
        }
        dialog?.show()
    }

    fun isShowing():Boolean{
       return if (dialog != null)
            dialog!!.isShowing
        else
            false
    }

    fun hideDialog() {
        dialog?.dismiss()
    }

}