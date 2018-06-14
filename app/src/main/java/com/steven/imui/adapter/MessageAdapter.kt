package com.steven.imui.adapter

import android.annotation.SuppressLint
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steven.imui.*
import com.steven.imui.bean.Message
import com.steven.imui.bean.MessageType
import com.steven.imui.dp2px
import kotlinx.android.synthetic.main.message_item_audio_send.view.*
import kotlinx.android.synthetic.main.message_item_image_send.view.*
import kotlinx.android.synthetic.main.message_item_location_send.view.*
import kotlinx.android.synthetic.main.message_item_system.view.*
import kotlinx.android.synthetic.main.message_item_text_send.view.*
import kotlinx.android.synthetic.main.message_slice_avatar_layout.view.*
import kotlinx.android.synthetic.main.message_slice_timestamp.view.*

class MessageAdapter(private val glide: GlideRequests, private val userId: String) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

  private var list: MutableList<Message>? = null

  fun setList(list: MutableList<Message>) {
    this.list = list
    notifyDataSetChanged()
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(getItemLayout(viewType), parent, false)
    return ViewHolder(itemView)
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val message = list?.get(position)!!
    val senderId = message.senderId
    with(holder.itemView) {
      if (getItemViewType(position) != SYSTEM) {
        if (position != 0 && message.timestamp - list?.get(position - 1)!!.timestamp <= 2 * 60000) {
          timestampTextView.visibility = View.GONE
        } else {
          timestampTextView.text = formatTimestamp(message.timestamp)
          timestampTextView.visibility = View.VISIBLE
        }
        avatarImageView.setImageResource(if (senderId == userId) android.R.color.holo_blue_dark else android.R.color.holo_green_dark)
        avatarTextView.text = senderId[senderId.lastIndex].toUpperCase().toString()
      }
      val text = message.text
      val fileId = message.fileId
      when (getItemViewType(position)) {
        TEXT_SEND, TEXT_RECEIVE -> mTextView.text = text
        FILE_SEND, FILE_RECEIVE -> mTextView.text = message.fileName
        AUDIO_SEND, AUDIO_RECEIVE -> audioTextView.text = "${message.duration}''"
        IMAGE_SEND, IMAGE_RECEIVE, VIDEO_SEND, VIDEO_RECEIVE -> glide.asBitmap().load(fileId).into(GlideZoom(mImageView))
        LOCATION_SEND, LOCATION_RECEIVE -> {
          locationTextView.text = text
          glide.load(fileId).override(dp2px(context, 240), dp2px(context, 100)).centerCrop().into(locationImageView)
        }
        else -> systemTextView.text = text
      }
    }
  }

  override fun getItemViewType(position: Int): Int {
    return list?.get(position)!!.let {
      val senderId = it.senderId
      when (it.type) {
        MessageType.TEXT -> if (senderId == userId) TEXT_SEND else TEXT_RECEIVE
        MessageType.AUDIO -> if (senderId == userId) AUDIO_SEND else AUDIO_RECEIVE
        MessageType.IMAGE -> if (senderId == userId) IMAGE_SEND else IMAGE_RECEIVE
        MessageType.LOCATION -> if (senderId == userId) LOCATION_SEND else LOCATION_RECEIVE
        MessageType.FILE -> if (senderId == userId) FILE_SEND else FILE_RECEIVE
        MessageType.VIDEO -> if (senderId == userId) VIDEO_SEND else VIDEO_RECEIVE
        else -> SYSTEM
      }
    }
  }

  override fun getItemCount(): Int {
    return if (list == null) 0 else list!!.size
  }

  @LayoutRes
  private fun getItemLayout(viewType: Int): Int {
    return when (viewType) {
      TEXT_SEND -> R.layout.message_item_text_send
      TEXT_RECEIVE -> R.layout.message_item_text_receive
      AUDIO_SEND -> R.layout.message_item_audio_send
      AUDIO_RECEIVE -> R.layout.message_item_audio_receive
      IMAGE_SEND -> R.layout.message_item_image_send
      IMAGE_RECEIVE -> R.layout.message_item_image_receive
      FILE_SEND -> R.layout.message_item_file_send
      FILE_RECEIVE -> R.layout.message_item_file_receive
      LOCATION_SEND -> R.layout.message_item_location_send
      LOCATION_RECEIVE -> R.layout.message_item_location_receive
      VIDEO_SEND -> R.layout.message_item_video_send
      VIDEO_RECEIVE -> R.layout.message_item_video_receive
      else -> R.layout.message_item_system
    }
  }

  companion object {
    const val TEXT_SEND = 0
    const val TEXT_RECEIVE = 1
    const val AUDIO_SEND = 2
    const val AUDIO_RECEIVE = 3
    const val IMAGE_SEND = 4
    const val IMAGE_RECEIVE = 5
    const val LOCATION_SEND = 6
    const val LOCATION_RECEIVE = 7
    const val FILE_SEND = 8
    const val FILE_RECEIVE = 9
    const val VIDEO_SEND = 10
    const val VIDEO_RECEIVE = 11
    const val SYSTEM = 12
  }
}