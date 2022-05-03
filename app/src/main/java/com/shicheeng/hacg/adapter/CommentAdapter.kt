package com.shicheeng.hacg.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shicheeng.hacg.R
import com.shicheeng.hacg.data.CommentData

class CommentAdapter(private val mutableList: MutableList<CommentData>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textComment: TextView = itemView.findViewById(R.id.comment_text)
        val nameComment: TextView = itemView.findViewById(R.id.comment_name)
        val imageComment: ImageView = itemView.findViewById(R.id.image_header)
        val dateComment: TextView = itemView.findViewById(R.id.comment_date)
        val likeComment: TextView = itemView.findViewById(R.id.comment_like_it)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_comment_dialog_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.dateComment.text = mutableList[position].date
        holder.nameComment.text = mutableList[position].name
        holder.likeComment.text = mutableList[position].likeIt
        holder.textComment.text = mutableList[position].comment
        Glide.with(holder.itemView.context).load(mutableList[position].imageUrl)
            .into(holder.imageComment)
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }


}