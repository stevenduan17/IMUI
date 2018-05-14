package com.steven.imui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.MotionEvent
import com.steven.imui.AudioRecorder


/**
 * @author Steven Duan
 * @since 2018/5/7
 * @version 1.0
 */
class RecordingButton : AppCompatButton, AudioRecorder.OnAudioRecordListener {

  constructor(context: Context) : super(context) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init()
  }

  companion object {
    private const val STATE_NORMAL = 1
    private const val STATE_RECORDING = 2
    private const val STATE_CANCEL = 3
    private const val STATE_SHORT_TIME = 4
  }

  private val mRecorder: AudioRecorder by lazy { AudioRecorder.getInstance(context.externalCacheDir) }
  private var mState = STATE_NORMAL
  var mStateListener: OnStateChangeListener? = null

  private fun init() {
    setOnLongClickListener {
      startRecord()
      false
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val x = event.x.toInt()
    val y = event.y.toInt()
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        //Real record starting is onLongClick
        mStateListener?.onTip()
        //for keep pressed state.
        this.isSelected = true
      }
      MotionEvent.ACTION_MOVE -> {
        if (isOutOfRegion(x, y)) {
          if (mState == STATE_RECORDING) {
            mState = STATE_CANCEL
            mStateListener?.onCancel()
          }
        } else {
          if (mState == STATE_CANCEL) {
            mState = STATE_RECORDING
            mStateListener?.onRecording()
          }
        }
      }
      MotionEvent.ACTION_UP -> {
        this.isSelected = false
        when (mState) {
          STATE_RECORDING -> {
            //record finished
            mRecorder.finish(AudioRecorder.NORMAL_FINISH)
          }
          STATE_CANCEL -> {
            //cancel record
            mRecorder.finish(AudioRecorder.FORCE_FINISH)
          }
        }
        //for show short time tip
        if (mState == STATE_SHORT_TIME) Handler().postDelayed({ mStateListener?.onNormal() }, 300) else mStateListener?.onNormal()
      }
    }
    return super.onTouchEvent(event)
  }

  private fun isOutOfRegion(x: Int, y: Int): Boolean {
    return x < 0 || x > measuredWidth || y < 0 || y > measuredHeight
  }

  private fun startRecord() {
    val vibrator = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      vibrator.vibrate(VibrationEffect.createOneShot(100L, 2))
    } else {
      @Suppress("DEPRECATION")
      vibrator.vibrate(100, AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
    }
    mRecorder.start(this)
    mState = STATE_RECORDING
    mStateListener?.onRecording()
  }

  override fun onShortTime() {
    mState = STATE_SHORT_TIME
    mStateListener?.onShortTime()
  }

  override fun onStop(duration: Long, path: String) {
    mStateListener?.onRecordDone(duration, path)
  }

  override fun onDurationReached(duration: Int, path: String) {
    mStateListener?.onDurationReached(duration, path)
  }

  interface OnStateChangeListener {
    fun onNormal()
    fun onTip()
    fun onRecording()
    fun onCancel()
    fun onShortTime()
    fun onDurationReached(duration: Int, path: String)
    fun onRecordDone(duration: Long, path: String)
  }
}