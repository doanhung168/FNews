package com.poly_team.fnews.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "news")
data class News(


    @PrimaryKey(autoGenerate = false)
    var id: String = "",

    @Ignore
    var avatar: String? = null,

    @ColumnInfo(name = "tilte")
    var title: String? = null,

    @ColumnInfo(name = "content")
    var content: String? = null,

    @Ignore
    var field: String? = null,

    @Ignore
    var state: String? = null,

    @Ignore
    var stateExtra: String? = null,

    @Ignore
    var active: Boolean = true,

    @Ignore
    var comment: List<String>? = null,

    @Ignore
    var tags: List<String>? = null,

    @Ignore
    var author: String? = null,

    @Ignore
    var like: Int = 0,

    @Ignore
    var time: Long = 0,

    @Ignore
    var isSaved: Boolean = false,

    @Ignore
    var view : Long = 0
) : Parcelable