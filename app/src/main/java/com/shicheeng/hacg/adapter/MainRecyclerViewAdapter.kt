package com.shicheeng.hacg.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shicheeng.hacg.PreviewActivity
import com.shicheeng.hacg.R
import com.shicheeng.hacg.adapter.MainRecyclerViewAdapter.NewHolder
import com.shicheeng.hacg.data.MainListData

class MainRecyclerViewAdapter(private val mutableList: MutableList<MainListData>) :
    RecyclerView.Adapter<NewHolder>() {
    class NewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headImage: ImageView = itemView.findViewById(R.id.list_item_head_image)
        val titleTextView: TextView = itemView.findViewById(R.id.list_item_title_text)
        val secondaryTextView: TextView = itemView.findViewById(R.id.list_item_secondary_text)
        val supportingTextView: TextView = itemView.findViewById(R.id.list_item_supporting_text)
        val tagRecyclerView: RecyclerView = itemView.findViewById(R.id.list_item_tage_chips)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NewHolder(view)
    }

    override fun onBindViewHolder(holder: NewHolder, position: Int) {

        val adapter = TagsChipAdapter(mutableList[position].arrayList)
        val layoutManager =
            LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)

        Glide.with(holder.itemView).load(mutableList[position].imageUrl).into(holder.headImage)
        holder.titleTextView.text = mutableList[position].title
        holder.secondaryTextView.text = mutableList[position].secondary
        holder.supportingTextView.text = mutableList[position].supportingText

        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.setClass(holder.itemView.context, PreviewActivity::class.java)
            intent.putExtra("NEXT_URL", mutableList[position].nextUrl)
            holder.itemView.context.startActivity(intent)
        }

        holder.tagRecyclerView.apply {
            setAdapter(adapter)
            setLayoutManager(layoutManager)
        }

    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}