package com.steven.imui

import android.content.Context

/**
 * @author Steven Duan
 * @since 2018/6/14
 * @version 1.0
 */
fun dp2px(context: Context,dp:Int):Int{
  val density = context.resources.displayMetrics.density
  return (dp*density+0.5F).toInt()
}