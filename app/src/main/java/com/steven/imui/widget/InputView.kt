package com.steven.imui.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.steven.imui.R
import kotlinx.android.synthetic.main.widget_layout_input.view.*


/**
 * IM message inputView
 * @author Steven Duan
 * @since 2018/4/27
 * @version 1.0
 */
class InputView : LinearLayout {

  constructor(context: Context) : super(context) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init()
  }

  private var mActionListener: OnInputActionListener? = null

  private fun init() {
    View.inflate(context, R.layout.widget_layout_input, this)
    mInputEditText.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(p0: Editable?) {
        p0?.let {
          val isEmpty = it.isEmpty()
          mSendImageView.visibility = if (isEmpty) View.GONE else View.VISIBLE
          mShowMoreImageView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
      }

      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })

    mInputEditText.setOnFocusChangeListener({ _, isFocus ->
      if (isFocus && mMoreLayout.visibility == View.VISIBLE) mMoreLayout.visibility = View.GONE
    })

    mShowMoreImageView.setOnClickListener {
      mInputEditText.clearFocus()
      hideSoftInput()
      mMoreLayout.visibility = if (mMoreLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
      if (mRecordButton.visibility == View.VISIBLE) {
        mRecordButton.visibility = View.GONE
        mAudioImageView.visibility = View.VISIBLE
        mKeyboardImageView.visibility = View.GONE
        mInputEditText.visibility = View.VISIBLE
      }
    }

    mAudioImageView.setOnClickListener({
      it.visibility = View.GONE
      mKeyboardImageView.visibility = View.VISIBLE
      mInputEditText.visibility = View.GONE
      if (!mInputEditText.text.isNullOrEmpty()) {
        mSendImageView.visibility = View.GONE
        mShowMoreImageView.visibility = View.VISIBLE
        hideSoftInput()
      }
      mRecordButton.visibility = View.VISIBLE
      hideMoreLayout()
    })

    mKeyboardImageView.setOnClickListener({
      it.visibility = View.GONE
      if (!mInputEditText.text.isNullOrEmpty()) {
        mSendImageView.visibility = View.VISIBLE
        mShowMoreImageView.visibility = View.GONE
      }
      mAudioImageView.visibility = View.VISIBLE
      mRecordButton.visibility = View.GONE
      mInputEditText.visibility = View.VISIBLE
      hideMoreLayout()
    })

    //perform item click(only for functional)
    mSendImageView.setOnClickListener { mActionListener?.onSendTextMessage(mInputEditText.text.toString()) }
    mLocationTextView.setOnClickListener { mActionListener?.onActionLocation() }
    mImageTextView.setOnClickListener { mActionListener?.onActionImage() }
    mFileTextView.setOnClickListener { mActionListener?.onActionFile() }
    mVideoTextView.setOnClickListener { mActionListener?.onActionVideo() }
  }

  private fun hideMoreLayout() {
    if (mMoreLayout.visibility == View.VISIBLE) {
      mMoreLayout.visibility = View.GONE
    }
  }

  private fun hideSoftInput() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
  }

  fun setRecordingButtonText(text: String) {
    mRecordButton.text = text
  }

  fun setAudioRecordListener(listener: RecordingButton.OnStateChangeListener) {
    mRecordButton.mStateListener = listener
  }

  fun setOnInputActionListener(listener: OnInputActionListener) {
    this.mActionListener = listener
  }

  interface OnInputActionListener {
    fun onSendTextMessage(text: String)
    fun onActionLocation()
    fun onActionImage()
    fun onActionFile()
    fun onActionVideo()
  }
}

