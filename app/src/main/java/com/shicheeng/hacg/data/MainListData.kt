package com.shicheeng.hacg.data

import org.jsoup.select.Elements

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
    val reply: String,
    val comments: Elements?,
)

data class TagPathData(val url: String, val nameTag: String)

data class SearchResultData(
    val title: String,
    val secondary: String,
    val bodyText: String,
    val path: String,
)

enum class CodeError {
    TIME_OUT,
    SOMETHING_ERROR


}
