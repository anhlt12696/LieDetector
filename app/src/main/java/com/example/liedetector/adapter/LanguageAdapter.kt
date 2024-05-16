package com.example.liedetector.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.liedetector.model.Language
import com.pgs.lie.detector.prank.test.fingerprint.scanner.R
import com.pgs.lie.detector.prank.test.fingerprint.scanner.databinding.ItemLanguageBinding

class LanguageAdapter(val context: Context, private val onclick: onClickLanguage
) : RecyclerView.Adapter<LanguageAdapter.ViewHoler>() {
    private var selectedPosition: Int = 0
    private var title: ArrayList<Language> = ArrayList()
    private var image: ArrayList<Int> = ArrayList()

    fun setData(dataTitle: ArrayList<Language>, dataImage: ArrayList<Int>) {
        title.clear()
        title.addAll(dataTitle)
        image.clear()
        image.addAll(dataImage)
        notifyDataSetChanged()
    }

    fun updateData(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    class ViewHoler(binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemLanguageBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoler {
        return ViewHoler(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHoler, position: Int) {
        val language = title.get(position)
        holder.binding.title.text = language.getName()
        Glide.with(context).load(image[position]).into(holder.binding.imaLanguage)
        if (selectedPosition == position) {
            holder.binding.rlView.setBackgroundResource(R.drawable.btn_chose_language)
        } else {
            holder.binding.rlView.setBackgroundResource(R.drawable.bg_item_language)
        }
        holder.binding.title.isSelected = true
        holder.itemView.setOnClickListener {
            onclick.onClicked(position, language.getValue(), image[position], language.getName());
        }
    }

    override fun getItemCount(): Int {
        return title.size
    }

    interface onClickLanguage {
        fun onClicked(position: Int, name: String, img: Int, language_name: String)
    }
}

