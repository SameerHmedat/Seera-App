package com.example.seeragroup.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.seeragroup.R

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.comingsoon)
        .into(this)
}

fun ImageView.loadImage(link: Int) {
    Glide.with(this)
        .load(link)
        .into(this)
}
