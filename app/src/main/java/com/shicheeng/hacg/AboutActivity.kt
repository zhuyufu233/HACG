package com.shicheeng.hacg

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.drakeet.about.AbsAboutActivity
import com.drakeet.about.Category
import com.drakeet.about.Contributor
import com.drakeet.about.License

class AboutActivity : AbsAboutActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreateHeader(icon: ImageView, slogan: TextView, version: TextView) {
        icon.setImageResource(R.mipmap.ic_launcher_llss_round)
        slogan.text = getString(R.string.slogan)
        version.text = "版本 ${BuildConfig.VERSION_NAME}"
        toolbar.navigationIcon = AppCompatResources
            .getDrawable(this, R.drawable.ic_back_arrow)
        toolbar.navigationIcon?.setTint(R.attr.iconColor)
    }


    override fun onItemsCreated(items: MutableList<Any>) {

        items.add(Category("开发人员"))
        items.add(Contributor(R.drawable.ic_avatar_shicheeng,
            "ShihCheeng",
            "主要开发人员（就我一个）而且穷",
            "https://github.com/shizheng233"))

        items.add(Category("开源许可和鸣谢"))
        items.add(License("琉璃神社",
            "最新的ACG资讯 分享同人动漫的快乐",
            "网站",
            "https://www.hacg.cat/wp/"))

        items.add(License("MultiType",
            "drakeet",
            License.APACHE_2,
            "https://github.com/drakeet/MultiType"))

        items.add(License("about-page",
            "drakeet",
            License.APACHE_2,
            "https://github.com/drakeet/about-page"))

        items.add(License("html-text", "wangchenyan", License.APACHE_2,
            "https://github.com/wangchenyan/html-text"))

        items.add(License("Jsoup", "jhy", License.MIT,
            "https://github.com/jhy/jsoup"))

        items.add(License("Glide",
            "bumptech",
            "BSD, part MIT and Apache 2.0. See the LICENSE file for details.",
            "https://github.com/bumptech/glide"))

        items.add(License("Subsampling Scale Image View",
            "davemorrissey",
            License.APACHE_2,
            "https://github.com/davemorrissey/subsampling-scale-image-view"))

        items.add(License("material-components",
            "material-components",
            License.APACHE_2,
            "https://github.com/material-components/material-components"))

    }
}