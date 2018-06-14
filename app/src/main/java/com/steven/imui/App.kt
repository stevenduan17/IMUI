package com.steven.imui

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author Steven Duan
 * @since 2018/6/12
 * @version 1.0
 */
@Suppress("unused")
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    Realm.init(this)
    Realm.setDefaultConfiguration(
        RealmConfiguration
            .Builder()
            .name("IM.realm")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
    )
  }
}