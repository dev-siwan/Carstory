package com.like.drive.motorfeed.ui.base.binder

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.base.ext.dpToPixel

@BindingAdapter("fitLoadImage")
fun ImageView.fitLoadImage(photoData: PhotoData?) {
    photoData?.let {
        Glide.with(context)
            .load(it.file ?: it.imgUrl)
            .transition(withCrossFade())
            .apply(RequestOptions().fitCenter())
            .into(this)
    }
}

@BindingAdapter("fitLoadPhotoView")
fun PhotoView.fitLoadImage(photoData: PhotoData?) {
    photoData?.let {
        Glide.with(context)
            .load(it.file ?: it.imgUrl)
            .transition(withCrossFade())
            .apply(RequestOptions().fitCenter())
            .into(this)
    }
}

@BindingAdapter("centerCropImage")
fun ImageView.centerCrop(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(context)
            .load(it)
            .centerCrop()
            .into(this)
    }
}

@BindingAdapter("uri")
fun ImageView.setUri(uri: Uri?) {
    val size = context.dpToPixel(100f).toInt()

    Glide.with(context)
        .load(uri)
        .transition(withCrossFade())
        .override(size)
        .into(this)
}

@BindingAdapter("uploadPhoto")
fun ImageView.setPhotoData(photoData: PhotoData?) {
    photoData?.let {
        val size = context.dpToPixel(80f).toInt()
        val glideOption: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)

        Glide.with(context)
            .load(it.file ?: it.imgUrl)
            .transition(withCrossFade())
            .apply(
                RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(context.dpToPixel(4.0f).toInt()))
            )
            .apply(glideOption)
            .override(size)
            .into(this)
    }
}

@BindingAdapter("profileImg")
fun ImageView.setProfileImg(path: String?) {

    val value = path?.let {
        if (it.startsWith("/user/")) {
            Firebase.storage.reference.child((it))
        } else {
            it
        }
    }

    Glide.with(context).load(value).apply(
        RequestOptions()
            .error(R.drawable.profile_default_img_100)
            .placeholder(R.drawable.profile_default_img_100)
            .transform(CircleCrop(), CenterCrop())
    )
        .transition(withCrossFade())
        .into(this@setProfileImg)

}


