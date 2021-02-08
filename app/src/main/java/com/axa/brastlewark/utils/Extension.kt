package com.axa.brastlewark.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.CircleCrop

fun ImageView.loadCircularImage(url: String, view: View) {
    val glideUrl = GlideUrl(
        url, LazyHeaders.Builder()
            .addHeader(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36"
            )
            .build()
    )

    Glide.with(view)
        .load(glideUrl)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .thumbnail(0.5f)
        .transform(CircleCrop())
        .into(this)
}