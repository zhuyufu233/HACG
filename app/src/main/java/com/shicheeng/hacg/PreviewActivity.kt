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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.shicheeng.hacg.databinding.ActivityPreviewBinding
import com.shicheeng.hacg.fm.CommentDialogFragment
import com.shicheeng.hacg.vm.PreViewModel
import me.wcy.htmltext.HtmlImageLoader
import me.wcy.htmltext.HtmlText
import me.wcy.htmltext.OnTagClickListener
import kotlin.concurrent.thread

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private val viewModel: PreViewModel by viewModels()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        val view = binding.root
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(view)
        val url: String = intent.getStringExtra("NEXT_URL")!!
        viewModel.onLoadHtmlData(url)
        viewModel.htmlData.observe(this) {
            val textView = binding.previewWebView
            textView.movementMethod = LinkMovementMethod.getInstance()
            HtmlText.from(it)
                .setImageLoader(object : HtmlImageLoader {

                    override fun loadImage(url: String?, p1: HtmlImageLoader.Callback?) {

                        Glide.with(this@PreviewActivity).asBitmap().load(url)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?,
                                ) {
                                    p1?.onLoadComplete(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    p1?.onLoadFailed()
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

                    override fun getMaxWidth(): Int {
                        return textView.width
                    }

                    override fun fitWidth(): Boolean {
                        return true
                    }

                }).setOnTagClickListener(object : OnTagClickListener {

                    override fun onImageClick(
                        context: Context?,
                        list: MutableList<String>?,
                        positon: Int,
                    ) {
                        val intent = Intent()
                        intent.setClass(this@PreviewActivity, ImageActivity::class.java)
                        intent.putExtra("IMAGE_URL", list!![positon])
                        startActivity(intent)
                    }

                    override fun onLinkClick(context: Context?, url: String?) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }

                }).into(textView)
        }
        viewModel.showBar.observe(this) {
            if (!it) {
                binding.previewProgressIndicator.visibility = View.GONE
            }
        }
        binding.previewToolBar.setNavigationOnClickListener {
            finish()
        }


        binding.fabComment.setOnClickListener {
            val dialog = CommentDialogFragment.newInstance(url)
            dialog.show(supportFragmentManager, CommentDialogFragment.TAG)
        }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v: View, windowInsetsCompat: WindowInsetsCompat ->

            val insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                rightMargin = insets.right
            }
            binding.previewWebView.updatePadding(bottom = insets.bottom)
            binding.fabComment.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            binding.previewAppbarLayout.updatePadding(top = insets.top)

            WindowInsetsCompat.CONSUMED
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        thread {
            Glide.get(this).clearDiskCache()
        }
        Glide.get(this).clearMemory()

    }

}