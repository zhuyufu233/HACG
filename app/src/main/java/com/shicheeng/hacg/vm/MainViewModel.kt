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
import java.net.SocketTimeoutException

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

            try {
                val elements: Elements = WebParser.getMainContext(url)
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

                //当抛出超时错误时
            } catch (e: SocketTimeoutException) {

                _message.postValue("超时")
                _showBar.postValue(false)

                //当抛出其他错误时
            } catch (e: Exception) {

                _message.postValue("出现了错误")
                _showBar.postValue(false)
            }

            _showBottomBar.postValue(false)
        }

    }

    fun setBottomBarShow(barShow: Boolean) {
        _showBottomBar.postValue(barShow)
    }

    fun setIndicationShow(isShow: Boolean) {
        _showBar.postValue(isShow)
    }


}