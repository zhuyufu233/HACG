package com.shicheeng.hacg.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shicheeng.hacg.api.WebParser
import com.shicheeng.hacg.data.SearchResultData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.HttpStatusException

class SearchViewModel : ViewModel() {

    private val _searchResult: MutableLiveData<ArrayList<SearchResultData>> = MutableLiveData()
    val searchResult: LiveData<ArrayList<SearchResultData>> = _searchResult

    private val _errorCode: MutableLiveData<Int> = MutableLiveData()
    val errorCode: LiveData<Int> = _errorCode

    private val _indicatorShow: MutableLiveData<Boolean> = MutableLiveData()
    val indicatorShow: LiveData<Boolean> = _indicatorShow

    private val _bottomIndicationShow: MutableLiveData<Boolean> = MutableLiveData()
    val bottomIndicationShow: LiveData<Boolean> = _bottomIndicationShow

    fun searchText(page: Int, text: String) {
        viewModelScope.launch {

            try {

                val elements = WebParser.getSearchData(page, text)
                val con = ArrayList<SearchResultData>()
                if (!elements.isEmpty()) {
                    for (element in elements) {
                        val title = element.getElementsByClass("entry-title").text()
                        if (title.isNotEmpty()) {
                            val subTitle = WebParser.parserSecondary(element)
                            val bodyText = WebParser.parserSearchView(element)
                            val nextUrl = WebParser.parserNextUrl(element)
                            val data = SearchResultData(title, subTitle, bodyText, nextUrl)
                            con.add(data)
                        }
                    }
                    //判断圆形指示器是否该显示
                    _searchResult.postValue(con)
                    delay(120)
                    _indicatorShow.postValue(false)

                    _bottomIndicationShow.postValue(false)
                }

            } catch (e: HttpStatusException) {

                if (e.statusCode == 404) {
                    _errorCode.postValue(404)
                }

            } catch (e: Exception) {
                _errorCode.postValue(1)
            }
        }
    }

    fun barVis(boolean: Boolean) {
        _indicatorShow.postValue(boolean)
    }

    fun bottomBarVis(boolean: Boolean) {
        _bottomIndicationShow.postValue(boolean)
    }

}