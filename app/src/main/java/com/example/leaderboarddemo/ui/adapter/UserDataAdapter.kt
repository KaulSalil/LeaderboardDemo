package com.example.leaderboarddemo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.leaderboarddemo.R
import com.example.leaderboarddemo.data.UserData
import com.example.leaderboarddemo.databinding.LayoutUserItemBinding
import com.example.leaderboarddemo.ui.viewmodel.MainViewModel
import com.example.leaderboarddemo.utils.convertToOrdinal
import com.example.leaderboarddemo.utils.loadUserImage

class UserDataAdapter(
    private val viewModel: MainViewModel,
    var userItmList: ArrayList<UserData>
) : RecyclerView.Adapter<UserDataAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding: LayoutUserItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_user_item, parent, false)
        return UserViewHolder(binding.root, binding)
    }

    override fun getItemCount() = userItmList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userItemData = userItmList[position]
        holder.binding.viewModel = viewModel
        holder.binding.data = userItemData

        userItemData.imageUrl?.let {url->
            loadUserImage(holder.binding.root.context, url, holder.userImage)
        }
        userItemData.rank?.let {
            holder.title.text =it.toString()
            holder.ordinal.text = convertToOrdinal(it)
        }
        userItemData.name?.let {
            holder.userName.text = it.toString()
        }
        userItemData.points?.let {
            holder.points.text = it.toString()+" "+holder.binding.root.context.resources.getString(R.string.points)
        }
        userItemData.price?.let {
            holder.price.text = holder.binding.root.context.resources.getString(R.string.rupees_symbol)+it.toString()
        }

    }

    class UserViewHolder(itemView: View, var binding: LayoutUserItemBinding) :
        RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val title: TextView = itemView.findViewById(R.id.title)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val points: TextView = itemView.findViewById(R.id.points)
        val price: TextView = itemView.findViewById(R.id.price)
        val ordinal: TextView = itemView.findViewById(R.id.ordinal)
        val topLayout: LinearLayout = itemView.findViewById(R.id.topLayout)
    }
}
