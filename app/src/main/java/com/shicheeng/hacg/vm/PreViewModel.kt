package com.shicheeng.hacg.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shicheeng.hacg.api.WebParser
import com.shicheeng.hacg.data.CodeError
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class PreViewModel : ViewModel() {

    private val _htmlData: MutableLiveData<String> = MutableLiveData()
    private val _htmlTitle: MutableLiveData<String> = MutableLiveData()
    private val _htmlTime: MutableLiveData<String> = MutableLiveData()
    private val _showBar: MutableLiveData<Boolean> = MutableLiveData()
    private val _htmlError: MutableLiveData<Int> = MutableLiveData()

    val htmlData: LiveData<String> = _htmlData
    val showBar: LiveData<Boolean> = _showBar
    val htmlTitle: LiveData<String> = _htmlTitle
    val htmlTime: LiveData<String> = _htmlTime
    val htmlError: LiveData<Int> = _htmlError

    fun onLoadHtmlData(url: String) {
        viewModelScope.launch {
            try {
                val element = WebParser.getMainContext(url)[0]
                val string: String = WebParser.parserHtmlText(element)
                val title: String = WebParser.parserHtmlTitle(element)
                val time: String = WebParser.parserHtmlTime(element)
                _showBar.postValue(false)
                delay(300)
                _htmlData.postValue(string)
                _htmlTime.postValue(time)
                _htmlTitle.postValue(title)
            } catch (e: SocketTimeoutException) {
                _htmlError.postValue(CodeError.TIME_OUT.ordinal)
                _showBar.postValue(false)
                return@launch
            } catch (e: Exception) {
                _htmlError.postValue(CodeError.SOMETHING_ERROR.ordinal)
                _showBar.postValue(false)
                return@launch
            }


        }
    }


}