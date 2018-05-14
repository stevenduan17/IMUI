package com.steven.imui

import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.util.*

/**
 * @author Steven Duan
 * @since 2018/5/7
 * @version 1.0
 */
@Suppress("PrivatePropertyName")
class AudioRecorder private constructor(private var dir: File) {

  private val TAG = AudioRecorder::class.java.simpleName

  companion object {
    const val DURATION_REACHED_FINISH = 1
    const val FORCE_FINISH = 2
    const val NORMAL_FINISH = 3

    private var instance: AudioRecorder? = null
    fun getInstance(dir: File): AudioRecorder {
      if (instance == null) {
        synchronized(AudioRecorder::class.java) {
          if (instance == null) {
            instance = AudioRecorder(dir)
          }
        }
      }
      return instance as AudioRecorder
    }
  }

  private var mRecorder: MediaRecorder? = null
  private val MAX_DURATION: Int = 60000
  private var mStartMillis: Long? = null
  private var listener: OnAudioRecordListener? = null
  private var path: String? = null

  fun start(listener: OnAudioRecordListener) {
    mRecorder?.let { finish(FORCE_FINISH) }
    this.listener = listener
    if (!dir.exists()) dir.mkdirs()
    path = File(dir, String.format("%s.aac", UUID.randomUUID().toString())).absolutePath
    mRecorder = MediaRecorder()
    mRecorder?.apply {
      setAudioSource(MediaRecorder.AudioSource.MIC)
      setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
      setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
      setOutputFile(path!!)
      setMaxDuration(MAX_DURATION)
      setOnInfoListener({ _, what, _ ->
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
          Log.d(TAG, "Max duration reached.")
          finish(DURATION_REACHED_FINISH)
        }
      })
      prepare()
      start()
      mStartMillis = System.currentTimeMillis()
    }
  }

  fun finish(type: Int) {
    mRecorder?.apply {
      try {
        setOnInfoListener(null)
        stop()
        when (type) {
          FORCE_FINISH -> deleteFile()
          DURATION_REACHED_FINISH -> listener?.onDurationReached(MAX_DURATION, path!!)
          else -> {
            if (mStartMillis != null) {
              val duration = System.currentTimeMillis() - mStartMillis!!
              if (duration < 1000) {
                Log.d(TAG, "Recording time is too short!")
                listener?.onShortTime()
                deleteFile()
              } else {
                listener?.onStop(duration, path!!)
              }
            }
          }
        }
      } catch (e: Exception) {
        Log.e(TAG, e.message)
        listener?.onShortTime()
        deleteFile()
      } finally {
        this.release()
        mRecorder = null
        listener = null
        path = null
        mStartMillis = null
      }
    }
  }

  private fun deleteFile() {
    path?.let {
      val file = File(it)
      if (file.exists()) file.delete()
    }
  }

  interface OnAudioRecordListener {
    fun onShortTime()
    fun onStop(duration: Long, path: String)
    fun onDurationReached(duration: Int, path: String)
  }
}