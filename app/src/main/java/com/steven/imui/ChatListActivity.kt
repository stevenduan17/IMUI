package com.steven.imui

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.steven.imui.adapter.ChatAdapter
import com.steven.imui.bean.Chat
import kotlinx.android.synthetic.main.activity_chat_list.*
import me.weyye.hipermission.HiPermission
import me.weyye.hipermission.PermissionCallback
import me.weyye.hipermission.PermissionItem

/**
 * 会话列表
 *
 * @author Steven Duan
 * @since 2018/4/12
 * @version 1.0
 */
class ChatListActivity : AppCompatActivity() {

  @Suppress("PrivatePropertyName")
  private val TAG = ChatListActivity::class.java.simpleName
  private val mAdapter: ChatAdapter by lazy { ChatAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_list)

    val permissions = mutableListOf(
        PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_storage), R.drawable.permission_ic_storage),
        PermissionItem(Manifest.permission.RECORD_AUDIO, getString(R.string.permission_audio_record), R.drawable.permission_ic_micro_phone)
    )
    HiPermission.create(this)
        .title(getString(R.string.permission_request_title))
        .msg(getString(R.string.permission_desc))
        .permissions(permissions)
        .style(R.style.PermissionDefaultNormalStyle)
        .animStyle(R.style.PermissionAnimFade)
        .checkMutiPermission(object : PermissionCallback {
          override fun onFinish() {
            Log.d(TAG, "All permission are granted.")
            initData()
          }

          override fun onDeny(permission: String?, position: Int) {
            Log.d(TAG, "Some permissions have been denied.")
          }

          override fun onGuarantee(permission: String?, position: Int) {
          }

          override fun onClose() {
            Log.d(TAG, "Permission check has been closed.")
          }
        })
  }

  private fun initData() {
    mRecyclerView.setHasFixedSize(true)
    mRecyclerView.addItemDecoration(DividerItemDecoration(this@ChatListActivity, DividerItemDecoration.VERTICAL))
    mRecyclerView.layoutManager = LinearLayoutManager(this@ChatListActivity)
    mRecyclerView.adapter = mAdapter

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
    }, 1000)
  }
}