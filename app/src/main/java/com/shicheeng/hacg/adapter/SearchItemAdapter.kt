package com.shicheeng.hacg.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shicheeng.hacg.PreviewActivity
import com.shicheeng.hacg.R
import com.shicheeng.hacg.adapter.SearchItemAdapter.ItemViewHolder
import com.shicheeng.hacg.data.SearchResultData

class SearchItemAdapter(private val list: ArrayList<SearchResultData>) :
    RecyclerView.Adapter<ItemViewHolder>() {


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val subText: TextView = itemView.findViewById(R.id.subtitle_text)
        val bodyText: TextView = itemView.findViewById(R.id.body_text)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.titleText.text = list[position].title
        holder.subText.text = list[position].secondary
        holder.bodyText.text = list[position].bodyText
        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.setClass(holder.itemView.context, PreviewActivity::class.java)
            intent.putExtra("NEXT_URL", list[position].path)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size


}