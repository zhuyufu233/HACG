package com.shicheeng.hacg.fm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shicheeng.hacg.R
import com.shicheeng.hacg.adapter.MagnetAdapter

class MagnetListDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(list: ArrayList<String>): MagnetListDialogFragment {
            val args = Bundle()
            args.putStringArrayList("LIST", list)
            val fragment = MagnetListDialogFragment()
            fragment.arguments = args
            return fragment
        }

        const val TAG = "LIST_MAGNET_FM"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var textTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val v = inflater.inflate(R.layout.dialog_bottom_magnet_lab, container, false)
        recyclerView = v.findViewById(R.id.dialog_bottom_recycler_view)
        textTitle = v.findViewById(R.id.dialog_bottom_text_title)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrayList = arguments?.getStringArrayList("LIST")!!
        val lm = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val ad = MagnetAdapter(arrayList)
        recyclerView.apply {
            adapter = ad
            layoutManager = lm
        }
        textTitle.text = getString(R.string.magnet_search_result, arrayList.size.toString())
    }

}