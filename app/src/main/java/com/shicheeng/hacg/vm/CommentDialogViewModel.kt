package com.shicheeng.hacg.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shicheeng.hacg.api.HttpTool
import com.shicheeng.hacg.api.WebParser
import com.shicheeng.hacg.data.CommentData
import kotlinx.coroutines.launch

class CommentDialogViewModel : ViewModel() {

    private val _loadComment: MutableLiveData<ArrayList<CommentData>> = MutableLiveData()
    val loadComment: LiveData<ArrayList<CommentData>> = _loadComment

    private val _indicatorShow: MutableLiveData<Boolean> = MutableLiveData()
    val indicator: LiveData<Boolean> = _indicatorShow

    private val _tipText: MutableLiveData<String> = MutableLiveData()
    val tipText: LiveData<String> = _tipText

    fun onLoadComment(url: String) {

        viewModelScope.launch {
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
                val comData = CommentData(
                    parserCommentImageUrl,
                    parserCommentName,
                    parserCommentDate,
                    parserComment,
                    HttpTool.someOneUpOrDown(parserCommentUpNum)
                )
                arrayList.add(comData)

            }
            _indicatorShow.postValue(false)
            _loadComment.postValue(arrayList)
        }

    }

}