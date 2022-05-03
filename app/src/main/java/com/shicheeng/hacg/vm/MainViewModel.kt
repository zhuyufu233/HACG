package com.shicheeng.hacg.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shicheeng.hacg.api.WebParser
import com.shicheeng.hacg.data.MainListData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.select.Elements

class MainViewModel : ViewModel() {

    private val _elementsLive: MutableLiveData<ArrayList<MainListData>> = MutableLiveData()
    val elementsLive: LiveData<ArrayList<MainListData>> = _elementsLive

    private val _showBar: MutableLiveData<Boolean> = MutableLiveData()
    val showBar: LiveData<Boolean> = _showBar

    private val _showBottomBar: MutableLiveData<Boolean> = MutableLiveData()
    val showBottomBar: LiveData<Boolean> = _showBottomBar

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message


    fun loadElementsData(url: String) {
        viewModelScope.launch {
            val elements: Elements = WebParser.getMainContext(url)!!
            if (elements.isNullOrEmpty()) {
                _message.postValue("超时")
                return@launch
            }
            val collections = ArrayList<MainListData>()

            for (element in elements) {
                val title: String =
                    element.getElementsByClass("entry-title")[0]
                        .getElementsByTag("a").text()
                if (title.isNotEmpty()) {
                    val secondaryText: String = WebParser.parserSecondary(element)
                    val imageUrl: String = WebParser.parserImageUrl(element)
                    val supportingText = WebParser.parserSupportingText(element)
                    val contentUrl = WebParser.parserNextUrl(element)
                    val tags = WebParser.parserFooterTag(element)
                    val mainListData =
                        MainListData(imageUrl,
                            title,
                            secondaryText,
                            supportingText,
                            contentUrl,
                            tags)

                    collections.add(mainListData)
                }
            }
            _showBar.postValue(false)
            delay(150)
            _elementsLive.postValue(collections)
            _showBottomBar.postValue(false)
        }

    }

    fun setBottomBarShow(barShow: Boolean) {
        _showBottomBar.postValue(barShow)
    }


}