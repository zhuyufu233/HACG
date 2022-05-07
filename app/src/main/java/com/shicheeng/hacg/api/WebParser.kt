package com.shicheeng.hacg.api

import com.shicheeng.hacg.data.TagPathData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.SocketTimeoutException

object WebParser {

    suspend fun getMainContext(url: String): Elements? {
        return withContext(Dispatchers.IO) {
            try {
                val element: Element = HttpTool.parserUrl(url)!!
                val elements: Elements = element.getElementsByTag("article")
                elements
            } catch (e: SocketTimeoutException) {
                null
            }

        }

    }

    suspend fun getCommentsText(url: String): Elements {
        return withContext(Dispatchers.IO) {
            val element: Element = HttpTool.parserUrl(url)!!
            val element2: Elements = element.getElementById("comments")!!
                .getElementById("wpdcom")!!
                .getElementById("wpd-threads")!!
                .getElementsByClass("wpd-thread-list")[0]
                .getElementsByClass("wpd-comment wpd_comment_level-1")

            element2

        }
    }

    fun parserSecondary(element: Element): String {
        val e: Element = element.getElementsByClass("entry-meta")[0]
        val byWho = e.getElementsByTag("span")[0].text()
        val time = e.getElementsByTag("a")[0].attr("title")
        val date = e.getElementsByTag("a")[0].getElementsByTag("time")[0].text()
        val authorName =
            e.getElementsByClass("author vcard")[0].getElementsByTag("a")[0].text()
        return "$byWho$date $time 经由$authorName"

    }

    fun parserFooterTag(element: Element): ArrayList<TagPathData>? {
        val e: Element = element.getElementsByTag("footer")[0]
        return if (e.getElementsByClass("tag-links").size != 0) {
            val whatTag = e.getElementsByClass("tag-links")[0]
            val elements = whatTag.getElementsByTag("a")
            val arrayList = ArrayList<TagPathData>()
            for (e2 in elements) {
                val tagPathData = TagPathData(e2.attr("href"), e2.text())
                arrayList.add(tagPathData)
            }
            arrayList
        } else {
            null
        }

    }

    fun parserImageUrl(element: Element): String {
        val e: Element = element.getElementsByClass("entry-content")[0].getElementsByTag("p")[0]
        return e.getElementsByTag("img")[0].attr("src")
    }

    fun parserSupportingText(element: Element): String {
        val e: Elements = element.getElementsByClass("entry-content")[0].getElementsByTag("p")
        var string = ""
        if (e.size == 1) {
            string = e[0].text().replace("<br>", "\n")
        } else {
            for (y in e) {
                string += y.text() + "\n"
            }
        }
        return string
    }

    fun parserNextUrl(element: Element): String {
        val e = element.getElementsByClass("entry-title")[0].getElementsByTag("a")
        return e.attr("href")
    }

    fun parserHtmlText(element: Element): String {
        val htmlHeader: String = element.getElementsByTag("header")[0].html()
        val htmlBodyPre: Element = element.getElementsByClass("entry-content")[0]
        val htmlBodyPre1 = htmlBodyPre.getElementsByTag("div")
        htmlBodyPre1[htmlBodyPre1.size - 1].remove()
        htmlBodyPre1[htmlBodyPre1.size - 2].remove()

        return "$htmlHeader \n<div>\n $htmlBodyPre1"
    }

    fun parserComment(element: Element): String {
        val elementMain = element.getElementsByTag("div")[0]
        val elementSecond = elementMain.getElementsByTag("div")[0]
            .getElementsByTag("div")[0]

        return elementSecond.getElementsByClass("wpd-comment-text")[0].text()
    }

    fun parserCommentHeader(element: Element): String {
        val elementMain = element.getElementsByTag("div")[0]
        val elementSecond = elementMain.getElementsByTag("div")[0]
            .getElementsByTag("div")[0]
        return elementSecond.getElementsByClass("wpd-comment-header")[0]
            .getElementsByClass("wpd-avatar")[0]
            .getElementsByTag("img")[0].attr("src")
    }

    fun parserCommentName(element: Element): String {
        val elementMain = element.getElementsByTag("div")[0]
        val elementSecond = elementMain.getElementsByTag("div")[0]
            .getElementsByTag("div")[0]
        return elementSecond.getElementsByClass("wpd-user-info")[0]
            .getElementsByClass("wpd-uinfo-top")[0]
            .getElementsByClass("wpd-comment-author")[0].text()
    }

    fun parserCommentDate(element: Element): String {
        val elementMain = element.getElementsByTag("div")[0]
        val elementSecond = elementMain.getElementsByTag("div")[0]
            .getElementsByTag("div")[0]

        return elementSecond.getElementsByClass("wpd-user-info")[0]
            .getElementsByClass("wpd-uinfo-bottom")[0]
            .getElementsByClass("wpd-comment-date")[0].text()
    }

    fun parserCommentUpNum(element: Element): String {
        val elementMain = element.getElementsByTag("div")[0]
        val elementSecond = elementMain.getElementsByTag("div")[0]
            .getElementsByTag("div")[0]

        return elementSecond.getElementsByClass("wpd-comment-footer")[0]
            .getElementsByClass("wpd-vote")[0]
            .getElementsByClass("wpd-vote-result")[0].text()
    }

}