package com.hexflake.rajutaskevince.ui.adapters

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hexflake.rajutaskevince.database.UserModel


@BindingAdapter("app:image")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    var requestOptions = RequestOptions()
    requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
    imageUrl?.let {
        Glide.with(imageView.context)
            .load(it)
            .centerInside()
            .apply(requestOptions)
            .into(imageView)
    }
}

@BindingAdapter("app:setUserInfo")
fun setUserInfo(textview: TextView, model: UserModel) {
    model.apply {
         buildString {
             append(first_name+" "+ last_name).append("\n")
             append(email)
    }.run(textview::setText)
    }
}

@BindingAdapter("app:isShoworNot")
fun isShoworNot(textview: View, model: Boolean) {
    textview.isVisible=model
}