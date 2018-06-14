package com.steven.imui.bean

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Initialized with default values ensures that an empty constructor is generated.
 *
 * @author Steven Duan
 * @since 2018/5/14
 * @version 1.0
 */
open class Message(
    @PrimaryKey
    var id: String = "",
    var type: Int = 0,
    var timestamp: Long = 0,
    var senderId: String = "",
    var text: String? = null,
    var fileId: String? = null,
    var fileName: String? = null,
    var duration: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
) : RealmObject()