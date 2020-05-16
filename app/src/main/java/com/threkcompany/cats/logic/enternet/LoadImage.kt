package com.threkcompany.cats.logic.enternet

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.squareup.picasso.Picasso
import com.threkcompany.cats.R

interface LoadImage {

    fun load(view: ImageView, placeHolder: CircularProgressDrawable, url: String)
}

class LoadImagePicasso : LoadImage {

    override fun load(view: ImageView, placeHolder: CircularProgressDrawable, url: String) {
        Picasso.get().load(url).placeholder(placeHolder).error(R.drawable.ic_error_black_24dp).into(view)
    }
}