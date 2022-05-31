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
import com.shicheeng.hacg.api.HttpTool
import com.shicheeng.hacg.api.WebParser
import com.shicheeng.hacg.data.CommentData
import com.shicheeng.hacg.util.CommentSerializable
import com.shicheeng.hacg.vm.CommentDialogViewModel
import java.io.Serializable

class CommentDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "DIALOG_ONE"

        fun newInstance(serializable: Serializable): CommentDialogFragment {
            val args = Bundle()
            args.putSerializable("COMMENT_URL", serializable)
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
        savedInstanceState: Bundle?,
    ): View {
        //实例化，在这种情况下貌似不能使用ViewBinding
        val view: View = inflater.inflate(R.layout.layout_comment_dialog, container, false)
        commentText = view.findViewById(R.id.dialog_comment_text)
        commentRecyclerView = view.findViewById(R.id.dialog_recycler_view)
        commentIndicator = view.findViewById(R.id.indicator_dialog)

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //使用Serializable传入数据
        val commentsData = arguments?.getSerializable("COMMENT_URL")!! as CommentSerializable
        val commentElements = commentsData.elements

        val list = ArrayList<CommentData>()
        val adapter = CommentAdapter(list)
        val layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)

        for (element in commentElements) {
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
                HttpTool.someOneUpOrDown(parserCommentUpNum),
                "第二层",
                null
            )
            list.add(comData)

        }

        adapter.notifyDataSetChanged()

        commentRecyclerView.apply {
            setAdapter(adapter)
            setLayoutManager(layoutManager)
        }

    }


}