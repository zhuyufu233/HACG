package com.shicheeng.hacg.fm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.shicheeng.hacg.R
import com.shicheeng.hacg.adapter.CommentAdapter
import com.shicheeng.hacg.data.CommentData
import com.shicheeng.hacg.vm.CommentDialogViewModel

class CommentDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "DIALOG_ONE"

        fun newInstance(url: String): CommentDialogFragment {
            val args = Bundle()
            args.putString("COMMENT_URL", url)
            val fragment = CommentDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var commentText: TextView
    private lateinit var commentRecyclerView: RecyclerView
    private lateinit var commentIndicator: CircularProgressIndicator
    private val viewModel: CommentDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.layout_comment_dialog, container, false)
        commentText = view.findViewById(R.id.dialog_comment_text)
        commentRecyclerView = view.findViewById(R.id.dialog_recycler_view)
        commentIndicator = view.findViewById(R.id.indicator_dialog)
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString("COMMENT_URL")!!
        viewModel.onLoadComment(url)
        val list = ArrayList<CommentData>()
        val adapter = CommentAdapter(list)
        val layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        viewModel.loadComment.observe(viewLifecycleOwner) {
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }

        commentRecyclerView.apply {
            setAdapter(adapter)
            setLayoutManager(layoutManager)
        }
        viewModel.indicator.observe(viewLifecycleOwner) {
            if (!it) {
                commentIndicator.visibility = View.GONE
            }
        }
        viewModel.tipText.observe(viewLifecycleOwner) {
            commentText.text = it
        }
    }


}