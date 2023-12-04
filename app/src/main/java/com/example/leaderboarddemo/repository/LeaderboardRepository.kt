package com.example.leaderboarddemo.repository

import android.content.Context
import com.example.leaderboarddemo.data.LeaderBoardJsonData
import com.example.leaderboarddemo.data.UserData
import com.example.leaderboarddemo.utils.LeaderboardException
import com.example.leaderboarddemo.utils.loadJSONFromAsset
import org.json.JSONException
import org.json.JSONObject

class LeaderboardRepository {

    suspend fun fetchDataFromJson(context: Context, initial: Int, final: Int): Any{
        val leaderBoardJsonData = LeaderBoardJsonData()
        val userData = ArrayList<UserData>()
        return try {
            val obj = JSONObject(loadJSONFromAsset(context, "leaderboard.json"))
            val totalData = obj.optInt("total")
            val topPrizeData = obj.optString("topPrize")
            val resultArr = obj.optJSONArray("result")
            for (i in initial..final) {
                val stateJsonObj = resultArr.getJSONObject(i)
                val id = stateJsonObj.optInt("id")
                val rank = stateJsonObj.optInt("rank")
                val rankOrdinal = stateJsonObj.optString("rankOrdinal")
                val name = stateJsonObj.optString("name")
                val imageUrl = stateJsonObj.optString("imageUrl")
                val points = stateJsonObj.optInt("points")
                val price = stateJsonObj.optInt("price")
                val isUser = stateJsonObj.optBoolean("isUser")
                val stateItem = UserData(id, rank, rankOrdinal, name, imageUrl, points, price, isUser)
                userData.add(stateItem)
            }
            leaderBoardJsonData.apply {
                result = userData
                total = totalData
                topPrize = topPrizeData
            }
        }catch (e: JSONException) {
           LeaderboardException(e.localizedMessage)
        }
    }
}