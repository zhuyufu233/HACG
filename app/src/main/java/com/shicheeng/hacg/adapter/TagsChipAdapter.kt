package com.shicheeng.hacg.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.shicheeng.hacg.R
import com.shicheeng.hacg.adapter.TagsChipAdapter.ChipViewHolder
import com.shicheeng.hacg.data.TagPathData

class TagsChipAdapter(private val list: MutableList<TagPathData>) :
    RecyclerView.Adapter<ChipViewHolder>() {


    class ChipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chipTag: Chip = itemView.findViewById(R.id.inner_tag_chip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.inner_list_item, parent, false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.chipTag.text = list[position].nameTag
    }

    override fun getItemCount(): Int = list.size
}