package com.shicheeng.hacg.api

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.math.absoluteValue


object HttpTool {

    fun parserUrl(url: String): Element? {
        val doc: Document = Jsoup.connect(url).get()
        return doc.body().getElementById("page")
            ?.getElementById("main")
            ?.getElementById("primary")
            ?.getElementById("content")
    }

    fun checkIfHttp(url: String): String {
        val shin: String = if (url.startsWith("//")) {
            url.replace("//", "https://")
        } else {
            url
        }
        return shin
    }

    fun someOneUpOrDown(num: String): String {
        val u = num.toInt()
        return when {
            u > 0 -> {
                "有 $u 个人觉得这个评论很顶"
            }
            u == 0 -> {
                "没人顶踩这条评论"
            }
            u < 0 -> {
                "有 ${u.absoluteValue} 个人觉得这条评论不行"
            }
            else -> {
                "错误"
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun imageGet(context: Context, url: String): Bitmap {
        return withContext(Dispatchers.IO){
            Glide.with(context).asBitmap().load(url).submit().get()
        }
    }

}