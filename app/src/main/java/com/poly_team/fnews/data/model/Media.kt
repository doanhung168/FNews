package com.poly_team.fnews.data.model

data class Media(
    var id: String? = null,
    var avatar: String? = null,
    var title: String? = null,
    var content: String? = null,
    var field: String? = null,
    var type: String? = null,
    var state: String? = null,
    var active: Boolean = true,
    var comment: List<String>? = null,
    var tags: List<String>? = null,
    var author: String? = null,
    var like: Int = 0,
    var disLike : Int = 0,
    var time: Long = 0,
    var isSaved: Boolean = false
)