package com.shicheeng.hacg.data

data class MainListData(
    val imageUrl: String,
    val title: String,
    val secondary: String,
    val supportingText: String,
    val nextUrl: String,
    val arrayList: ArrayList<TagPathData>?,
)

data class CommentData(
    val imageUrl: String,
    val name: String,
    val date: String,
    val comment: String,
    val likeIt: String,
)

data class TagPathData(val url: String, val nameTag: String)
