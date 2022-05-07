package com.shicheeng.hacg.fm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shicheeng.hacg.adapter.MainRecyclerViewAdapter
import com.shicheeng.hacg.data.MainListData
import com.shicheeng.hacg.databinding.ListMainBinding
import com.shicheeng.hacg.vm.MainViewModel

class ListFragment : Fragment() {

    private var _binding: ListMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    companion object {

        fun newInstance(url: String): ListFragment {
            val args = Bundle()
            args.putString("KEY_URL", url)
            val fragment = ListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ListMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url: String = arguments?.getString("KEY_URL")!!
        viewModel.loadElementsData(url)
        val list = ArrayList<MainListData>()
        val layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        val adapter = MainRecyclerViewAdapter(list)

        ViewCompat.setOnApplyWindowInsetsListener(this.requireView()){ v: View, windowInsetsCompat: WindowInsetsCompat ->

            val insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.listRecyclerView.updatePadding(bottom = insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        viewModel.elementsLive.observe(viewLifecycleOwner) {
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }
        binding.listRecyclerView.apply {
            setAdapter(adapter)
            setLayoutManager(layoutManager)
        }

        binding.listRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var isSliding: Boolean = false
            var pageCount: Int = 1

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val linearLayoutManager: LinearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastCount = linearLayoutManager.findLastVisibleItemPosition()
                    val allCount = linearLayoutManager.itemCount
                    if (lastCount == (allCount - 1) && isSliding) {
                        pageCount += 1
                        val totalUrl = "${url}/page/${pageCount}"
                        viewModel.loadElementsData(totalUrl)
                        viewModel.setBottomBarShow(true)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isSliding = dy > 0

            }
        })

        viewModel.showBar.observe(viewLifecycleOwner) {
            if (!it) {
                binding.listProgressIndicator.visibility = View.GONE
            }
        }

        viewModel.showBottomBar.observe(viewLifecycleOwner) {
            if (!it) {
                binding.listBottomIndication.visibility = View.GONE
            } else {
                binding.listBottomIndication.visibility = View.VISIBLE
            }
        }
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
        }

    }

}