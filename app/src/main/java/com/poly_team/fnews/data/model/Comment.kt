package com.poly_team.fnews.data.model

data class Comment(
    var id: String = "",
    var ownerId: String = "",
    var ownerAvatar : String = "",
    var ownerName: String = "",
    var time: Long = -1,
    var like: Long = -1,
    var content : String = "",
    var childComment: List<String>? = null,
)
