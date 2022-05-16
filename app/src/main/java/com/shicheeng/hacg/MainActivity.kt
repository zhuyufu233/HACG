package com.shicheeng.hacg

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shicheeng.hacg.databinding.ActivityMainBinding
import com.shicheeng.hacg.fm.ListFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val list = listOf<Fragment>(
            ListFragment.newInstance(MyApp.mainUrl),
            ListFragment.newInstance(MyApp.ageUrl),
            ListFragment.newInstance(MyApp.animeUrl),
            ListFragment.newInstance(MyApp.comicUrl),
            ListFragment.newInstance(MyApp.gameUrl)
        )

        setSupportActionBar(binding.mainToolBar)
        val tab = binding.mainTabLayout
        val viewPager2 = binding.mainViewPager2
        viewPager2.adapter = MyAdapter(supportFragmentManager, list)
        viewPager2.offscreenPageLimit = 1
        TabLayoutMediator(tab, viewPager2) { tabLayout: TabLayout.Tab, i: Int ->
            tabLayout.text = MyApp.textList[i]
        }.attach()

        ViewCompat.setOnApplyWindowInsetsListener(view) { v: View, windowInsetsCompat: WindowInsetsCompat ->

            val insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                rightMargin = insets.right
            }
            binding.mainAppLayoutBar.updatePadding(top = insets.top)

            WindowInsetsCompat.CONSUMED
        }


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_on_main -> {
                val intent = intent
                intent.setClass(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    inner class MyAdapter(
        fragmentManager: FragmentManager,
        private val list: List<Fragment>,
    ) :
        FragmentStateAdapter(fragmentManager, this.lifecycle) {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(position: Int): Fragment {
            return list[position]
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