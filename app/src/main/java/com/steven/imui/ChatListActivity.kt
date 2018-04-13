package com.steven.imui

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.steven.imui.adapter.ChatAdapter
import com.steven.imui.bean.Chat
import kotlinx.android.synthetic.main.activity_chat_list.*

/**
 * 会话列表
 *
 * @author Steven Duan
 * @since 2018/4/12
 * @version 1.0
 */
class ChatListActivity : AppCompatActivity() {

  private val mAdapter: ChatAdapter by lazy { ChatAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_list)

    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    mRecyclerView.setHasFixedSize(true)
    mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    mRecyclerView.layoutManager = LinearLayoutManager(this)
    mRecyclerView.adapter = mAdapter
    initData()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item!!.itemId == android.R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun initData() {
    val list = mutableListOf(
        Chat("01", "Steven", "Nice to meet u!", 2, System.currentTimeMillis()),
        Chat("02", "Kevin", "Do u want 2 play basketball tonight?", 10, System.currentTimeMillis() - 30000),
        Chat("03", "We are family", "Haa,I'm coming!", 0, System.currentTimeMillis() - 50000),
        Chat("04", "Christina", "You know what I'm felling.", 0, System.currentTimeMillis() - 100000000),
        Chat("05", "Joey", "Enn,'Friends' is very nice.", 0, System.currentTimeMillis() - 200000000),
        Chat("06", "Big big world", "Welcome to our big family!", 0, System.currentTimeMillis() - 5000000000)
    )
    Handler().postDelayed({
      mAdapter.setList(list)
      mLoadingLayout.visibility = View.GONE
    }, 3000)
  }
}