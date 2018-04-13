package com.steven.imui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    chatListButton.setOnClickListener { startActivity(Intent(this, ChatListActivity::class.java)) }
    chatActivityButton.setOnClickListener { startActivity(Intent(this,ChatActivity::class.java)) }
  }
}


