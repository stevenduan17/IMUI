package com.steven.imui

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.util.Log
import android.webkit.MimeTypeMap




/**
 * @author Steven Duan
 * @since 2018/5/4
 * @version 1.0
 */
fun getVideoThumbnail(path: String): Bitmap? {
  var bitmap: Bitmap? = null
  val mmr = MediaMetadataRetriever()
  try {
    mmr.setDataSource(path)
    bitmap = mmr.frameAtTime
    mmr.release()
  } catch (e: Exception) {
    Log.e("getVideoThumbnail", e.message)
  } finally {
    mmr.release()
  }
  return bitmap
}

fun getVideoThumb2(path: String, kind: Int): Bitmap {
  return ThumbnailUtils.createVideoThumbnail(path, kind)
}


/**
 * 获取文件的MimeType
 */
fun getMimeType(filePath: String): String? {
  val ext = MimeTypeMap.getFileExtensionFromUrl(filePath)
  return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
}
