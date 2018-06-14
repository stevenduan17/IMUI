package com.steven.imui

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.steven.imui.adapter.MessageAdapter
import com.steven.imui.bean.Message
import com.steven.imui.bean.MessageType
import com.steven.imui.widget.InputView
import com.steven.imui.widget.RecordingButton
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_chat.*


/**
 * @author Steven Duan
 * @since 2018/4/12
 * @version 1.0
 */
class ChatActivity : AppCompatActivity() {

  @Suppress("PrivatePropertyName")
  private val TAG = ChatActivity::class.java.simpleName

  private val mUserId = "test-a"

  companion object {
    const val CHAT_ID = "CHAT_ID"
  }

  private lateinit var mAdapter: MessageAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat)

    supportActionBar?.apply {
      title = "Janifer Bell"
      setDisplayHomeAsUpEnabled(true)
    }

    initInputView()
    initMessages()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == android.R.id.home) onBackPressed()
    return super.onOptionsItemSelected(item)
  }

  private fun initInputView() {
    mInputView.setAudioRecordListener(object : RecordingButton.OnStateChangeListener {
      override fun onNormal() {
        Log.d(TAG, "onNormal")
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
        Log.d(TAG, "send location")
      }


      override fun onActionImage() {
        Log.d(TAG, "send image")
      }

      override fun onActionFile() {
        Log.d(TAG, "send file")
      }

      override fun onActionVideo() {
        Log.d(TAG, "send video")
      }
    })
  }

  private fun initMessages() {
    //data for test
    val list = mutableListOf(
        Message("0", MessageType.TEXT, System.currentTimeMillis() - 1000000, "test-b", "Hello, nice to meet you!", null, null, null, null, null),
        Message("1", MessageType.TEXT, System.currentTimeMillis() - 800000, "test-a", "Nice to meet you,too!", null, null, null, null, null),
        Message("2", MessageType.IMAGE, System.currentTimeMillis() - 700000, "test-a", null, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528796827917&di=1259149e24fbdb02ce458b07dd32a441&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F96dda144ad345982b391b10900f431adcbef8415.jpg", null, null, null, null),
        Message("3", MessageType.IMAGE, System.currentTimeMillis() - 600000, "test-b", null, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528897126065&di=1dcdc76c90fc085ae7cb5379378b29b5&imgtype=0&src=http%3A%2F%2Fimg18.3lian.com%2Fd%2Ffile%2F201710%2F11%2F047e849fc1c15eb6268015c1911e6a26.jpg", null, null, null, null),
        Message("4", MessageType.LOCATION, System.currentTimeMillis() - 500000, "test-b", "Optics valley road, international business center,10089", "http://api.map.baidu.com/staticimage?width=500&height=300&center=116.413387,39.910924&zoom=16", null, null, null, null),
        Message("5", MessageType.FILE, System.currentTimeMillis() - 400000, "test-b", null, "https://github.com/realm/realm-java/blob/master/README.md", "READ_ME.md", null, null, null),
        Message("6", MessageType.SYSTEM, System.currentTimeMillis() - 300000, "test-b", "“Test B” has been invited to this group.", null, null, null, null, null),
        Message("7", MessageType.VIDEO, System.currentTimeMillis() - 200000, "test-a", null, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528889986099&di=ee831d88adbe848c73f6701e67711127&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F42a98226cffc1e1792fa64ac4690f603728de9e2.jpg", "demo.mp4", null, null, null)
    )
    Realm.getDefaultInstance().use { it.executeTransactionAsync({ r -> r.insertOrUpdate(list) }) }

    mRecyclerView.layoutManager = LinearLayoutManager(this)
    mRecyclerView.itemAnimator = DefaultItemAnimator()
    mAdapter = MessageAdapter(GlideApp.with(this), mUserId)
    mRecyclerView.adapter = mAdapter
    val messages = Realm.getDefaultInstance().use {
      val all = it.where(Message::class.java).sort("timestamp").findAll()
      it.copyFromRealm(all)
    }
    mAdapter.setList(messages)
  }
}