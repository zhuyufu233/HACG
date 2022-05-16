package com.shicheeng.hacg

import android.app.Application

class MyApp : Application() {

    // https://www.hacg.cat/wp/page/1?s=%E5%8A%A8%E7%94%BB&submit=%E6%90%9C%E7%B4%A2
    companion object {
        const val mainUrl: String = "https://www.hacg.cat/wp"
        const val ageUrl: String = "${mainUrl}/age.html"
        const val animeUrl: String = "${mainUrl}/anime.html"
        const val comicUrl: String = "${mainUrl}/comic.html"
        const val gameUrl: String = "${mainUrl}/game.html"
        const val searchUrl: String = "${mainUrl}/page/%s?s=%s&submit=搜索"


        val textList = listOf(
            "最新",
            "文章",
            "动画",
            "漫画",
            "游戏"
        )
    }

}