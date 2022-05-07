package com.shicheeng.hacg.vm

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shicheeng.hacg.api.HttpTool
import kotlinx.coroutines.launch

class ImageViewModel : ViewModel() {

    private val _loadBitmap: MutableLiveData<Bitmap> = MutableLiveData()
    val loadBitmap: LiveData<Bitmap> = _loadBitmap

    fun onLoadBitmap(context: Context, url: String) {
        viewModelScope.launch {
            val bitmap = HttpTool.imageGet(context, url)
            _loadBitmap.postValue(bitmap)
        }
    }

}