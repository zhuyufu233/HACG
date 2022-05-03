package com.shicheeng.hacg.vm


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shicheeng.hacg.api.WebParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PreViewModel : ViewModel() {

    private val _htmlData: MutableLiveData<String> = MutableLiveData()
    private val _showBar: MutableLiveData<Boolean> = MutableLiveData()

    val htmlData: LiveData<String> = _htmlData
    val showBar: LiveData<Boolean> = _showBar

    fun onLoadHtmlData(url: String) {
        viewModelScope.launch {
            val string: String = WebParser.parserHtmlText(WebParser.getMainContext(url)!![0])
            _showBar.postValue(false)
            delay(300)
            _htmlData.postValue(string)
        }
    }


}