package com.steven.imui

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.steven.imui.widget.InputView
import com.steven.imui.widget.RecordingButton
import kotlinx.android.synthetic.main.activity_chat.*

/**
 * @author Steven Duan
 * @since 2018/4/12
 * @version 1.0
 */
class ChatActivity : AppCompatActivity() {

  @Suppress("PrivatePropertyName")
  private val TAG = ChatActivity::class.java.simpleName

  companion object {
    const val CHAT_ID = "CHAT_ID"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat)

    initInputView()

  }

  private fun initInputView() {
    mInputView.setAudioRecordListener(object : RecordingButton.OnStateChangeListener {
      override fun onNormal() {
        Log.d(TAG,"onNormal")
        mAudioStateCardView.visibility = View.GONE
        mTipTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chat_ic_audio_record_0, 0, 0)
        mInputView.setRecordingButtonText(getString(R.string.tap_to_speak))
      }

      override fun onTip() {
        mInputView.setRecordingButtonText(getString(R.string.long_press_to_start_record))
        mTipTextView.text = getString(R.string.long_press_to_start_record)
      }

      override fun onRecording() {
        mAudioStateCardView.visibility = View.VISIBLE
        val text = getString(R.string.release_to_send_slide_to_cancel)
        mInputView.setRecordingButtonText(text)
        mTipTextView.text = text
        mTipTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chat_recording_animation, 0, 0)
        val animationDrawable = mTipTextView.compoundDrawables[1] as AnimationDrawable
        animationDrawable.start()
      }

      override fun onCancel() {
        Log.d(TAG, "onCancel")
        mTipTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chat_ic_audio_cancel, 0, 0)
        val text = getString(R.string.release_to_cancel)
        mInputView.setRecordingButtonText(text)
        mTipTextView.text = text
      }

      override fun onShortTime() {
        Log.d(TAG, "onShortTime")
        mTipTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.chat_ic_audio_shout_time, 0, 0)
        mTipTextView.text = getString(R.string.recording_time_too_short)
      }

      override fun onDurationReached(duration: Int, path: String) {
      }

      override fun onRecordDone(duration: Long, path: String) {
        Log.d(TAG, "Record duration ${duration / 1000}")
      }
    })

    mInputView.setOnInputActionListener(object : InputView.OnInputActionListener {
      override fun onSendTextMessage(text: String) {
        Log.d(TAG, "onSendTextMessage$text")
      }

      override fun onActionLocation() {
        Log.d(TAG,"send location")
      }


      override fun onActionImage() {
        Log.d(TAG,"send image")
      }

      override fun onActionFile() {
        Log.d(TAG,"send file")
      }

      override fun onActionVideo() {
        Log.d(TAG,"send video")
      }
    })
  }
}