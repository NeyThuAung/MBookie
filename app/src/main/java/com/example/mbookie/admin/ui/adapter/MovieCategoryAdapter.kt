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
import java.lang.Exception

class MovieCategoryAdapter(val mContext : Context,
                           val mLayoutResourseId : Int,
                           private val selectedPos: Int,
                           private val movieCategoryList : ArrayList<MovieCategoryData>) : ArrayAdapter<MovieCategoryData>(mContext,mLayoutResourseId,movieCategoryList) {

    private val tempUnitList = ArrayList<MovieCategoryData>(movieCategoryList)

    override fun getCount(): Int {
        return movieCategoryList.size
    }

    override fun getItem(position: Int): MovieCategoryData {
        return movieCategoryList[position]
    }

    override fun getItemId(position: Int): Long {
        return movieCategoryList[position].id.toLong()
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

                selectOneName?.text = productType.movieCategoryName
            }else{
                clProductType?.isVisible = true
                clSelectOneTitle?.isVisible = false

                productTypeName?.text = productType.movieCategoryName
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
        return convertTemp!!
    }



   private val filterResult = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val result = FilterResults()
            val suggestions = ArrayList<MovieCategoryData>()

            if (p0.isNullOrEmpty()) {
                suggestions.addAll(movieCategoryList)
                Log.d("Called once", "Called Once Onnce")
            } else {
                val stringPattern: String = p0.toString().lowercase().trim()
                for (item in movieCategoryList) {
                    if (item.movieCategoryName.lowercase().contains(stringPattern)) {
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
            movieCategoryList.clear()
            movieCategoryList.addAll(p1?.values as List<MovieCategoryData>)
            notifyDataSetChanged()
            Log.d("Sleepy", "Her is ${p1.values.toString()}")

        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            val temp = resultValue as MovieCategoryData
            Log.d("Sleepy1", "Her is $temp")
            return temp.movieCategoryName
        }
    }
}