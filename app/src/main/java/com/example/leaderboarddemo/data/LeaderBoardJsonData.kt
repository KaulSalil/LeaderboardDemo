package com.example.leaderboarddemo.data

data class LeaderBoardJsonData(
    var total: Int? = null,
    var result: ArrayList<UserData>? = null,
    var topPrize: String? = null
)
data class UserData(
    val id: Int? = null,
    val rank: Int? = null,
    val rankOrdinal: String? = null,
    val name: String? = null,
    val imageUrl: String? = null,
    val points: Int? = null,
    val price: Int? = null,
    val isUser: Boolean? = null,
)