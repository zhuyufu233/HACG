package com.shicheeng.hacg

import android.app.Application

class MyApp : Application() {

    companion object {
        const val mainUrl: String = "https://www.hacg.cat/wp"
        const val ageUrl: String = "${mainUrl}/age.html"
        const val animeUrl: String = "${mainUrl}/anime.html"
        const val comicUrl: String = "${mainUrl}/comic.html"
        const val gameUrl: String = "${mainUrl}/game.html"

        val textList = listOf(
            "最新",
            "文章",
            "动画",
            "漫画",
            "游戏"
        )
        const val jsVar: String =
            "<script type='text/javascript'> \nwindow.onload = function()\n{var \$img = document.getElementsByTagName('img');for(var p in  \$img){\$img[p].style.width = '100%'; \$img[p].style.height ='auto'}}</script>"
    }

}