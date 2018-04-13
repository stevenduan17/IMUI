package com.steven.imui.bean

/**
 * @author Steven Duan
 * @since 2018/4/12
 * @version 1.0
 */
data class Chat(val id: String, var name: String, var lastMessage: String, var unread: Int ,var time: Long)