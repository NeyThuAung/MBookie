package com.example.mbookie.admin.ui.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.mbookie.R
import com.example.mbookie.util.MovieCategoryData
import com.example.mbookie.util.TotalSeatData
import java.lang.Exception

class TotalSeatAdapter(val mContext : Context,
                       val mLayoutResourseId : Int,
                       private val selectedPos: Int,
                       private val totalSeatList : ArrayList<TotalSeatData>) : ArrayAdapter<TotalSeatData>(mContext,mLayoutResourseId,totalSeatList) {

    private val tempUnitList = ArrayList<TotalSeatData>(totalSeatList)

    override fun getCount(): Int {
        return totalSeatList.size
    }

    override fun getItem(position: Int): TotalSeatData {
        return totalSeatList[position]
    }

    override fun getItemId(position: Int): Long {
        return totalSeatList[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertTemp = convertView
        if (convertTemp ==null){
            val inflater= (mContext as Activity).layoutInflater
            convertTemp = inflater.inflate(mLayoutResourseId,parent,false)
        }

        try {
            val productType = getItem(position)
            val clSelectOneTitle = convertTemp?.findViewById<ConstraintLayout>(R.id.clSelectOne)
            val clProductType = convertTemp?.findViewById<ConstraintLayout>(R.id.clProductType)
            val view = convertTemp?.findViewById<View>(R.id.viewProductType)
            val productTypeName = convertTemp?.findViewById<TextView>(R.id.tvProductType)
            val selectOneName = convertTemp?.findViewById<TextView>(R.id.tvSelectOneTitle)
            val selectedMark = convertTemp?.findViewById<ImageView>(R.id.ivSelectedMark)

            if (productType.id == -1){
                clSelectOneTitle?.isVisible = true
                clProductType?.isVisible = false

                selectOneName?.text = productType.totalSeat.toString()
            }else{
                clProductType?.isVisible = true
                clSelectOneTitle?.isVisible = false

                productTypeName?.text = productType.totalSeat.toString()
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
        return convertTemp!!
    }



   private val filterResult = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val result = FilterResults()
            val suggestions = ArrayList<TotalSeatData>()

            if (p0.isNullOrEmpty()) {
                suggestions.addAll(totalSeatList)
                Log.d("Called once", "Called Once Onnce")
            } else {
                val stringPattern: String = p0.toString().lowercase().trim()
                for (item in totalSeatList) {
                    if (item.totalSeat.toString().lowercase().contains(stringPattern)) {
                        suggestions.add(item)
                    }
                }

            }
            result.count = suggestions.size
            result.values = suggestions
            Log.d("Sleepy2", "Her is ${result.values}")
            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            totalSeatList.clear()
            totalSeatList.addAll(p1?.values as List<TotalSeatData>)
            notifyDataSetChanged()
            Log.d("Sleepy", "Her is ${p1.values.toString()}")

        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            val temp = resultValue as TotalSeatData
            Log.d("Sleepy1", "Her is $temp")
            return temp.totalSeat.toString()
        }
    }
}