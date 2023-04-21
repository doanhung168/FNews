package com.poly_team.fnews.data.network

data class NetworkResponse(
    val success: Boolean,
    val message: String?,
    val data: Any?
)

fun makeAQueryMap(
    type: String, field: String, page: Int, perPage: Int
): Map<String, Any> {
    val queryMap = HashMap<String, Any>()
    queryMap["type"] = type
    queryMap["field"] = field
    queryMap["page"] = page
    queryMap["per_page"] = perPage
    return queryMap
}