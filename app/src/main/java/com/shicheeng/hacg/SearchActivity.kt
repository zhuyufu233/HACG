package com.shicheeng.hacg

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shicheeng.hacg.adapter.SearchItemAdapter
import com.shicheeng.hacg.data.SearchResultData
import com.shicheeng.hacg.databinding.AcivitySearchBinding
import com.shicheeng.hacg.vm.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: AcivitySearchBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = AcivitySearchBinding.inflate(layoutInflater)
        val viewRoot = binding.root
        super.onCreate(savedInstanceState)
        setContentView(viewRoot)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val vModel: SearchViewModel by viewModels()
        var page = 1
        ViewCompat.setOnApplyWindowInsetsListener(viewRoot) { view: View, windowInsetsCompat: WindowInsetsCompat ->

            val insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {

                rightMargin = insets.right
                leftMargin = insets.left
            }
            binding.searchAppBar.updatePadding(top = insets.top)


            WindowInsetsCompat.CONSUMED
        }

        val list = ArrayList<SearchResultData>()
        val adapterSearch = SearchItemAdapter(list)
        val linear = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.searchRecyclerView.apply {
            adapter = adapterSearch
            layoutManager = linear
        }

        //监听列表滑动
        binding.searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var isScrolling: Boolean = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastCount = manager.findLastVisibleItemPosition()
                    val allCount = manager.itemCount
                    if (lastCount == (allCount - 1) && isScrolling) {
                        page += 1
                        val text = binding.searchEditText.editableText.toString()
                        vModel.searchText(page, text)
                        vModel.bottomBarVis(true)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    isScrolling = true
                }
            }

        })

        //数据引入
        vModel.searchResult.observe(this) {
            list.addAll(it)
            adapterSearch.notifyDataSetChanged()
        }

        //观测
        vModel.indicatorShow.observe(this) {
            if (!it) {
                binding.searchCircularIndicator.visibility = View.GONE
            } else {
                binding.searchCircularIndicator.visibility = View.VISIBLE
            }
        }

        vModel.bottomIndicationShow.observe(this) {

            binding.searchBottomIndicator.apply {

                visibility = if (it) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        vModel.errorCode.observe(this) {
            //获取返回码
            when (it) {

                404 -> {
                    Snackbar.make(viewRoot,
                        getString(R.string.no_more, it.toString()),
                        Snackbar.LENGTH_LONG)
                        .show()
                    binding.searchBottomIndicator.visibility = View.GONE
                }

                1 -> {
                    Snackbar.make(viewRoot, "未知错误", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding.searchToolBar.setNavigationOnClickListener {
            finish()
        }

        binding.searchEditText.setOnEditorActionListener { p0, p1, _ ->

            if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                page = 1
                vModel.searchText(page, p0.text.toString())
                vModel.barVis(true)
                list.clear()
                adapterSearch.notifyDataSetChanged()
            }

            false
        }

    }

}

