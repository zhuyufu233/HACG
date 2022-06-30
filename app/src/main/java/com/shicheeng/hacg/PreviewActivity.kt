package com.shicheeng.hacg

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.shicheeng.hacg.adapter.CommentAdapter
import com.shicheeng.hacg.common.magnet
import com.shicheeng.hacg.data.CodeError
import com.shicheeng.hacg.databinding.ActivityPreviewBinding
import com.shicheeng.hacg.fm.CommentDialogFragment
import com.shicheeng.hacg.fm.MagnetListDialogFragment
import com.shicheeng.hacg.util.CommentSerializable
import com.shicheeng.hacg.vm.CommentDialogViewModel
import com.shicheeng.hacg.vm.PreViewModel
import me.wcy.htmltext.HtmlImageLoader
import me.wcy.htmltext.HtmlText
import me.wcy.htmltext.OnTagClickListener

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private val viewModel: PreViewModel by viewModels()
    private val viewModelOnComment: CommentDialogViewModel by viewModels()


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(view)
        val url: String = intent.getStringExtra("NEXT_URL")!!

        viewModel.onLoadHtmlData(url)
        viewModelOnComment.onLoadComment(url)

        viewModel.htmlTitle.observe(this) {
            binding.previewContextInclude.previewWebTitle.text = it
        }

        viewModel.htmlData.observe(this) {
            val text = binding.previewContextInclude.previewWebView
            text.movementMethod = LinkMovementMethod.getInstance()

            HtmlText.from(it).setImageLoader(object : HtmlImageLoader {

                override fun loadImage(url: String?, call: HtmlImageLoader.Callback?) {

                    Glide.with(this@PreviewActivity).asBitmap().load(url)
                        .into(object : CustomTarget<Bitmap>() {

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?,
                            ) {
                                call!!.onLoadComplete(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                call!!.onLoadFailed()
                            }

                        })
                }

                override fun getDefaultDrawable(): Drawable {
                    return ContextCompat.getDrawable(this@PreviewActivity,
                        R.drawable.ic_loading_image)!!
                }

                override fun getErrorDrawable(): Drawable {
                    return ContextCompat.getDrawable(this@PreviewActivity,
                        R.drawable.ic_loading_error)!!
                }

                override fun getMaxWidth(): Int = text.width

                override fun fitWidth(): Boolean = true

            }).setOnTagClickListener(object : OnTagClickListener {

                override fun onImageClick(
                    p1: Context,
                    list: MutableList<String>?,
                    positon: Int,
                ) {
                    val intent = Intent(this@PreviewActivity, ImageActivity::class.java)
                    intent.putExtra("IMAGE_URL", list!![positon])
                    startActivity(intent)
                }

                override fun onLinkClick(p0: Context?, p1: String?) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }

            }).into(text)

            binding.previewFab.setOnClickListener { _ ->
                val list = it.magnet().toList()
                val arrayList = ArrayList<String>()
                list.forEach { s ->
                    arrayList.add(s)
                }
                val magnetListDialogFragment = MagnetListDialogFragment.newInstance(arrayList)
                magnetListDialogFragment.show(supportFragmentManager, MagnetListDialogFragment.TAG)
            }
        }

        viewModel.htmlTime.observe(this) {
            binding.previewContextInclude.previewWebTime.text = it
        }

        viewModel.htmlError.observe(this) {
            when (it) {
                CodeError.SOMETHING_ERROR.ordinal -> {
                    Snackbar.make(view, "出现了某些问题", Snackbar.LENGTH_INDEFINITE).show()
                }
                CodeError.TIME_OUT.ordinal -> {
                    Snackbar.make(view, "超时", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        }



        viewModelOnComment.loadComment.observe(this) {
            val theAdapter = CommentAdapter(it)
            val recycler = binding.previewCommentInclude.previewCommentRecycler
            val layout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            recycler.apply {
                adapter = theAdapter
                layoutManager = layout
                isNestedScrollingEnabled = false
            }
            theAdapter.setOnItemClickListener { positon ->
                val commentSerializable = CommentSerializable()
                commentSerializable.elements = it[positon].comments
                CommentDialogFragment.newInstance(commentSerializable)
                    .show(supportFragmentManager, CommentDialogFragment.TAG)
            }
        }

        viewModelOnComment.tipText.observe(this) {
            binding.previewCommentInclude.previewCommentIsNo.apply {
                if (it.equals("无评论")) {
                    visibility = View.GONE
                    text = it
                }
            }
        }

        binding.previewToolBar.setNavigationOnClickListener {
            finish()
        }


        ViewCompat.setOnApplyWindowInsetsListener(view) { v: View, windowInsetsCompat: WindowInsetsCompat ->
            val insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                rightMargin = insets.right
            }
            binding.previewAppbarLayout.updatePadding(top = insets.top)
            binding.previewCommentInclude.root.updatePadding(bottom = insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        viewModel.showBar.observe(this) {

            binding.previewContextInclude.previewProgressIndicator.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModelOnComment.indicator.observe(this) {

            binding.previewCommentInclude.previewCommentIndication.visibility =
                if (it) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

    }

}