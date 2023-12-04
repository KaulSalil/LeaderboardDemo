package com.example.leaderboarddemo.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

fun loadJSONFromAsset(context: Context, fileName: String): String {
    var json = ""
    try {
        val `is` = context.assets.open(fileName)
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        json = String(buffer, charset("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return json
    }

    return json
}

fun loadUserImage(context: Context, userImageName: String, imageView: ImageView){
    Glide.with(context).load(getImage(userImageName, context)).circleCrop().into(imageView)
}
private fun getImage(imageName: String, context: Context): Int {
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}

fun convertToOrdinal(rank : Int): String{
    val suffixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
    return when (rank % 100) {
        11, 12, 13 -> "th"
        else -> suffixes[rank % 10]
    }
}