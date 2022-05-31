package com.shicheeng.hacg.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.shicheeng.hacg.R
import com.shicheeng.hacg.data.CommentData

class CommentAdapter(private val mutableList: MutableList<CommentData>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private lateinit var listener: (position: Int) -> Unit

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textComment: TextView = itemView.findViewById(R.id.comment_text)
        val nameComment: TextView = itemView.findViewById(R.id.comment_name)
        val imageComment: ImageView = itemView.findViewById(R.id.image_header)
        val dateComment: TextView = itemView.findViewById(R.id.comment_date)
        val likeComment: TextView = itemView.findViewById(R.id.comment_like_it)
        val replyComment: TextView = itemView.findViewById(R.id.comment_reply)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_comment_dialog_item, parent, false)
        return CommentViewHolder(view)
    }

    //主要
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        holder.dateComment.text = mutableList[position].date
        holder.nameComment.text = mutableList[position].name
        holder.likeComment.text = mutableList[position].likeIt
        holder.textComment.text = mutableList[position].comment

        //判断是否为第二层
        if (mutableList[position].reply == "第二层") {
            holder.replyComment.visibility = View.GONE
        } else {
            holder.replyComment.text = mutableList[position].reply
        }

        Glide.with(holder.itemView.context).load(mutableList[position].imageUrl)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(holder.imageComment)

        holder.itemView.setOnClickListener {

            //做出判断，若没有二楼评论就不做行为
            if (mutableList[position].comments.isNullOrEmpty()) {
                return@setOnClickListener
            }
            listener.invoke(position)
        }

    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        this.listener = listener
    }


}