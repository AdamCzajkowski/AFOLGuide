package com.example.mysets.ui.main.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("bindImagerRecycler")
    fun bindImageRecycler(imageView: ImageView, imageUrl: String?) {
        if (imageUrl == null) {
            Picasso.get()
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREfb58-te-dTEfAWLrWesITRdlqFVslyGNojhMntNXyNh3z11Y")
                .into(imageView)
        } else {
            Picasso.get().load("$imageUrl").into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("bindIntToString")
    fun bindIntToString(textView: TextView, int: Int) {
        textView.text = int.toString()
    }
}