package com.example.liedetector.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.liedetector.model.RowItem
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R

class RowAdapter(private val data: List<RowItem>) : RecyclerView.Adapter<RowAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.text



        // Set background color corresponding to position

        /*val backgroundColor = getColorForText(holder.itemView.context,position)
        holder.itemView.setBackgroundDrawable(backgroundColor)*/

    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getColorForText(context: Context,position: Int): Drawable? {
        // Customize background color based on text content
        return when (position) {
            0 -> ContextCompat.getDrawable(context, R.drawable.bg_truth)
            1 -> ContextCompat.getDrawable(context, R.drawable.bg_lie)
            2 -> ContextCompat.getDrawable(context, R.drawable.bg_not_tell)
            // Add more cases for other texts or use a dynamic approach
            else -> ContextCompat.getDrawable(context, R.drawable.bg_truth)
        }
    }
}