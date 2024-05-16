package com.example.liedetector.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.liedetector.model.RowItem
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R

class Row2PlayerAdapter(private val data: List<RowItem>) : RecyclerView.Adapter<Row2PlayerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_2_player_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Row2PlayerAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.text

        // Set background color corresponding to position
        /*val backgroundColor = getColorForText(holder.itemView.context,position)
        holder.itemView.setBackgroundDrawable(backgroundColor)*/
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)

    }
    private fun getColorForText(context: Context, position: Int): Drawable? {
        // Customize background color based on text content
        return when (position) {
            0 -> ContextCompat.getDrawable(context, R.drawable.bg_result_2_player_truth_2)
            1 -> ContextCompat.getDrawable(context, R.drawable.bg_result_2_player_lie_1)
            2 -> ContextCompat.getDrawable(context, R.drawable.bg_result_2_player_truth_2)
            // Add more cases for other texts or use a dynamic approach
            else -> ContextCompat.getDrawable(context, R.drawable.bg_result_2_player_truth_2)
        }
    }
}