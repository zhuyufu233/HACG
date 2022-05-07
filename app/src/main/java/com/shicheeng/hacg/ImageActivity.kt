package com.shicheeng.hacg

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.davemorrissey.labs.subscaleview.ImageSource
import com.shicheeng.hacg.databinding.ActivityImageBinding
import com.shicheeng.hacg.vm.ImageViewModel

class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding
    private val viewModel: ImageViewModel by viewModels()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityImageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val imageUrl = intent.getStringExtra("IMAGE_URL")!!
        viewModel.onLoadBitmap(this, imageUrl)
        viewModel.loadBitmap.observe(this) {
            binding.scaleImage.setImage(ImageSource.bitmap(it))
        }
        binding.fabBackButton.setOnClickListener {
            finish()
        }
        binding.scaleImage.setOnClickListener{
            finish()
        }

    }

}