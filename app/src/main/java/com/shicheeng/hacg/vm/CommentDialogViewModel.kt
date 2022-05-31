package com.shicheeng.hacg.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shicheeng.hacg.api.HttpTool
import com.shicheeng.hacg.api.WebParser
import com.shicheeng.hacg.data.CommentData
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CommentDialogViewModel : ViewModel() {

    private val _loadComment: MutableLiveData<ArrayList<CommentData>> = MutableLiveData()
    val loadComment: LiveData<ArrayList<CommentData>> = _loadComment

    private val _indicatorShow: MutableLiveData<Boolean> = MutableLiveData()
    val indicator: LiveData<Boolean> = _indicatorShow

    private val _tipText: MutableLiveData<String> = MutableLiveData()
    val tipText: LiveData<String> = _tipText

    //加载评论的数据
    fun onLoadComment(url: String) {

        viewModelScope.launch {

            try {
                val commentsText = WebParser.getCommentsText(url)
                if (commentsText.isEmpty()) {
                    _tipText.postValue("无评论")
                    return@launch
                }

                val arrayList = ArrayList<CommentData>()
                for (element in commentsText) {
                    val parserComment = WebParser.parserComment(element)
                    val parserCommentImageUrl =
                        HttpTool.checkIfHttp(WebParser.parserCommentHeader(element))
                    val parserCommentName = WebParser.parserCommentName(element)
                    val parserCommentDate = WebParser.parserCommentDate(element)
                    val parserCommentUpNum = WebParser.parserCommentUpNum(element)
                    val reply: String = if (WebParser.getCommentsTextLevel2(element).isEmpty()) {
                        "无评论"
                    } else {
                        "点击查看回复"
                    }

                    val comData = CommentData(
                        parserCommentImageUrl,
                        parserCommentName,
                        parserCommentDate,
                        parserComment,
                        HttpTool.someOneUpOrDown(parserCommentUpNum),
                        reply,
                        WebParser.getCommentsTextLevel2(element)
                    )
                    arrayList.add(comData)
                }

                _indicatorShow.postValue(false)
                _loadComment.postValue(arrayList)
            } catch (e: SocketTimeoutException) {
                _tipText.postValue("超时")
                _indicatorShow.postValue(false)
            } catch (e: Exception) {
                _tipText.postValue("出了些问题")
                _indicatorShow.postValue(false)
            }

        }

    }
}