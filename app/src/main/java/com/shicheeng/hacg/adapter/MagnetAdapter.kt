package com.shicheeng.hacg.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shicheeng.hacg.R
import com.shicheeng.hacg.adapter.MagnetAdapter.MagnetHolder
import com.shicheeng.hacg.databinding.ListMagnetTextBinding

class MagnetAdapter(private val list: ArrayList<String>) : RecyclerView.Adapter<MagnetHolder>() {

    class MagnetHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.list_magnet_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MagnetHolder {
        val binding =
            ListMagnetTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MagnetHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MagnetHolder, position: Int) {
        holder.text.text = holder.itemView.context.getString(R.string.magnet_head, list[position])
        holder.itemView.setOnClickListener {
            val clipboard =
                it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("${it.context.packageName}.copy",
                holder.itemView.context.getString(R.string.magnet_head, list[position]))
            clipboard.setPrimaryClip(clip)
            Snackbar.make(holder.itemView.rootView,
                holder.itemView.context.getString(R.string.magnet_copy),
                Snackbar.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int = list.size
}