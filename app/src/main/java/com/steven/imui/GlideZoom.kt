package com.steven.imui

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

@Suppress("PrivatePropertyName")
class GlideZoom(private val imageView: ImageView) : SimpleTarget<Bitmap>() {
  private val IMAGE_SIZE = dp2px(imageView.context, 240)
  override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
    val width = bitmap.width
    val height = bitmap.height
    val imageViewHeight: Int
    val imageViewWidth: Int
    if (width >= height) {
      imageViewWidth = Math.min(IMAGE_SIZE, width)
      val zW = imageViewWidth.toFloat() / width.toFloat()
      imageViewHeight = (height * zW).toInt()
    } else {
      imageViewHeight = Math.min(height, IMAGE_SIZE)
      val zH = imageViewHeight.toFloat() / height.toFloat()
      imageViewWidth = (width * zH).toInt()
    }
    val params = imageView.layoutParams
    params.height = imageViewHeight
    params.width = imageViewWidth
    imageView.layoutParams = params
    imageView.setImageBitmap(bitmap)
  }
}