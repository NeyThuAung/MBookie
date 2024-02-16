package com.example.mbookie.util

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.example.mbookie.R

class DeleteDialog(var activity: FragmentActivity) {
    var dialog: Dialog? = null
    private lateinit var btnOkay: Button
    private lateinit var btnCancel: Button
    private lateinit var tvMsg: TextView
    private lateinit var tvNote: TextView
    private lateinit var iconImg: ImageView
    private lateinit var imgClose: ImageButton

    fun showDialog(
        msg: String,
        alertButtonListener: AlertButtonListener,
        note: String = "",
        img: Int = R.drawable.ic_red_delete,
        okayTextColor: String = "#FF4d4d",
        tvOkayText: String = ""
    ) {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
                return
            } else {
                setData(msg, note, img, okayTextColor)
                dialog!!.show()
            }
        } else {

            dialog = Dialog(activity)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(R.layout.delete_dialog_layout)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog!!.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER

            dialog!!.window!!.attributes = lp

            btnOkay = dialog?.findViewById(R.id.mbDelete)!!
            btnCancel = dialog?.findViewById(R.id.mbDeleteDialogCancel)!!
            tvMsg = dialog?.findViewById(R.id.tvDeleteDialogTitle)!!
            tvNote = dialog?.findViewById(R.id.tvDeleteDialogDesc)!!
            iconImg = dialog?.findViewById(R.id.ivDeleteDialogAlarm)!!
            imgClose = dialog?.findViewById(R.id.ibDeleteDialogCancel)!!

            if (tvOkayText.isNotEmpty()) {
                btnOkay.text = tvOkayText
            }
            btnOkay.setBackgroundColor(Color.parseColor(okayTextColor))
            setData(msg, note, img, okayTextColor)


            imgClose.setOnClickListener {
                dialog?.hide()
            }

            btnOkay.setOnClickListener {
                dialog?.hide()
                alertButtonListener.onPositiveClick()
            }
            btnCancel.setOnClickListener {
                dialog?.hide()
                alertButtonListener.onCancelClick()
            }


            dialog?.show()
        }
    }

    private fun setData(
        msg: String,
        note: String,
        img: Int,
        okayTextColor: String
    ) {

        tvNote.isVisible = note.isNotEmpty()
        tvNote.text = note

        tvMsg.text = msg
        iconImg.setImageResource(img)
        btnOkay.setBackgroundColor(Color.parseColor(okayTextColor))
    }

    fun hideDialog() {
        if (dialog != null)
            dialog?.dismiss()
    }

    interface AlertButtonListener {
        fun onPositiveClick()
        fun onCancelClick()
    }

}


