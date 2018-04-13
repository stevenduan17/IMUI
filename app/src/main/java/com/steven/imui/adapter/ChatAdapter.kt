package com.steven.imui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.steven.imui.ChatActivity
import com.steven.imui.GlideApp
import com.steven.imui.R
import com.steven.imui.bean.Chat
import com.steven.imui.formatTimestamp
import kotlinx.android.synthetic.main.item_layout_chat.view.*
import org.jetbrains.anko.startActivity

/**
 * @author Steven Duan
 * @since 2018/4/12
 * @version 1.0
 */
class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

  private var list: List<Chat>? = null

  fun setList(list: List<Chat>) {
    this.list = list
    notifyDataSetChanged()
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_chat, parent, false)
    return ViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val chat = list!![position]
    with(holder.itemView) {
      chatNameTextView.text = chat.name
      timestampTextView.text = formatTimestamp(chat.time)
      lastMessageTextView.text = chat.lastMessage
      if (chat.unread == 0) {
        unreadCountTextView.visibility = View.GONE
      } else {
        unreadCountTextView.text = chat.unread.toString()
        unreadCountTextView.visibility = View.VISIBLE
      }
      GlideApp.with(context).load(R.drawable.google).circleCrop().into(avatarImageView)
      setOnClickListener({
        context.startActivity<ChatActivity>(ChatActivity.CHAT_ID to chat.id)
      })
    }
  }

  override fun getItemCount(): Int {
    return list?.size ?: 0
  }
}