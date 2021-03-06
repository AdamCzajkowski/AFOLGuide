package com.application.afol.ui.adapters

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.application.afol.utility.dropLastTwoChars
import com.application.afol.utility.setVisibility
import com.squareup.picasso.Picasso

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("bindImagerRecycler")
    fun bindImageRecycler(imageView: ImageView, imageUrl: String?) {
        if (imageUrl == null) {
            Picasso.get()
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREfb58-te-dTEfAWLrWesITRdlqFVslyGNojhMntNXyNh3z11Y")
                .fit()
                .centerCrop()
                .into(imageView)
        } else {
            Picasso
                .get()
                .load("$imageUrl")
                .fit()
                .centerInside()
                .into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("bindIntToString")
    fun bindIntToString(textView: TextView, int: Int) {
        textView.text = int.toString()
    }

    var bindURLParse: ((String) -> Unit)? = null
    var bindInstrctionURLParse: ((String) -> Unit)? = null


    @JvmStatic
    @BindingAdapter("bindURLa")
    fun bindURLbutton(button: Button, text: String) {
        button.setOnClickListener { bindURLParse?.invoke(text) }
    }

    @JvmStatic
    @BindingAdapter("bindShortNumber")
    fun TextView.bindShortNumber(set_num: String) {
        text = set_num.dropLastTwoChars()
    }

    @JvmStatic
    @BindingAdapter("bindInstruction")
    fun Button.bindInstructionURLbutton(text: String) {
        setOnClickListener { bindInstrctionURLParse?.invoke(text) }
    }

    var bindImageToUrl: ((String) -> Unit)? = null
    var bindMocToUrl: ((String) -> Unit)? = null
    var bindBLToIUrl: ((String) -> Unit)? = null
    var bindPartUrl: ((String) -> Unit)? = null

    @JvmStatic
    @BindingAdapter("bindImgToUrl")
    fun bindImgToUrl(imageView: ImageView, text: String?) {
        if (text != null) imageView.setOnClickListener { bindImageToUrl?.invoke(text) }
    }

    @JvmStatic
    @BindingAdapter("bindButtonToUrl")
    fun Button.bindButtonToUrl(text: String?) {
        if (text != null) setOnClickListener { bindImageToUrl?.invoke(text) }
    }

    @JvmStatic
    @BindingAdapter("bindBrickLink")
    fun Button.bindBrickLink(url: String?) {
        if (url != null) {
            setVisibility(true)
            setOnClickListener {
                bindBLToIUrl?.invoke(url)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("bindSparePart")
    fun TextView.bindSparePart(isSparePart: Boolean) {
        setVisibility(isSparePart)
    }

    @JvmStatic
    @BindingAdapter("bindMOCToURL")
    fun CardView.bindMOCToURL(url: String?) {
        if (url != null) setOnClickListener {
            bindMocToUrl?.invoke(url)
        }
    }

    @JvmStatic
    @BindingAdapter("bindPartToUrl")
    fun CardView.bindPartToUrl(url: String?) {
        if (url != null) setOnClickListener {
            bindPartUrl?.invoke(url)
        }
    }

    @JvmStatic
    @BindingAdapter("bindFavoriteIcon")
    fun AppCompatImageButton.bindFavoriteIcon(state: Boolean) {
        isSelected = state
    }
}