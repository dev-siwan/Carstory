package com.like.drive.motorfeed.ui.base.binder

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.like.drive.motorfeed.ui.base.ext.dpToPixel
import com.like.drive.motorfeed.data.photo.PhotoData

/*@BindingAdapter("loadImage")
fun ImageView.setLoadImage(imageUrl: String?) {
    //placeholder 이미지 가운데에 표시하기 위해
    scaleType = ImageView.ScaleType.CENTER
    setBackgroundColor(ContextCompat.getColor(context, R.color.pro_sky_pale))
    Glide.with(context)
        .load(imageUrl)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_empty_popup)
                .transform(CenterCrop(), RoundedCorners(context.dpToPixel(2.0f).toInt()))
        )
        .into(this)
}*/

@BindingAdapter("fitLoadImage")
fun ImageView.fitLoadImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .apply(RequestOptions().fitCenter())
        .into(this)
}

@BindingAdapter("centerCropImage")
fun ImageView.centerCrop(imageUrl: String?) {

    val glideOption: RequestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).centerCrop()

    Glide.with(context)
        .load(imageUrl)
        .apply(glideOption)
        .into(this)
}

@BindingAdapter("uri")
fun ImageView.setUri(uri: Uri?) {
    val size = context.dpToPixel(100f).toInt()

    val glideOption: RequestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)

    Glide.with(context)
        .load(uri)
        .transition(withCrossFade())
        .apply(glideOption)
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
            .load(it.file?:it.imgUrl)
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


/*@BindingAdapter("emailProfileImg")
fun ImageView.setProfileImg(email: String?) {
    val mp5Email: String? = email?.replaceMD5() ?: ""
    Glide.with(context).load(HttpDefine.PROFILE_IMG_HOST + mp5Email).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).apply(
        RequestOptions()
            .placeholder(R.drawable.profile_default_img_100)
            .transform(CircleCrop(),CenterCrop()))
        .into(this)
}*/


