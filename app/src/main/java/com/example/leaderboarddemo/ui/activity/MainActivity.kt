package com.example.leaderboarddemo.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leaderboarddemo.R
import com.example.leaderboarddemo.data.LeaderBoardJsonData
import com.example.leaderboarddemo.data.UserData
import com.example.leaderboarddemo.databinding.ActivityMainBinding
import com.example.leaderboarddemo.ui.BaseActivity
import com.example.leaderboarddemo.ui.BaseViewModel
import com.example.leaderboarddemo.ui.adapter.UserDataAdapter
import com.example.leaderboarddemo.ui.viewmodel.MainViewModel
import com.example.leaderboarddemo.ui.viewmodel.MainViewModel.Companion.ON_DATA_FAILURE
import com.example.leaderboarddemo.ui.viewmodelfactory.MainViewModelFactory
import com.example.leaderboarddemo.utils.convertToOrdinal
import com.example.leaderboarddemo.utils.loadUserImage
import com.examwarriors.notifiers.Notify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : BaseActivity() {

    override val kodein by kodein()
    private lateinit var mainViewModel: MainViewModel
    private val factory by instance<MainViewModelFactory>()
    private val binding: ActivityMainBinding by lazyBinding()
    override val dataBinding: Boolean = true
    override val layoutResource: Int = R.layout.activity_main
    override fun getViewModel(): BaseViewModel {
        return mainViewModel
    }
    private var userDataList = ArrayList<UserData>()
    private var finalCount = 0
    private var initial = 0
    private var final = 0
    private var limit = 50
    var offset = 1
    val userId = 1198
    private var isLoading = false
    private var mLayoutManager: LinearLayoutManager? = null

    override fun initializeViewModel() {
        mainViewModel =
            ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    override fun setBindings() {
        binding.viewModel = mainViewModel
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onNotificationReceived(data: Notify) {
        when(data.identifier){
            MainViewModel.ON_START -> {
                binding.progressAnimationView.visibility = View.VISIBLE
            }
            MainViewModel.ON_DATA_FETCH -> {
                val response = data.arguments[0] as LeaderBoardJsonData
                binding.progressAnimationView.visibility = View.GONE
                finalCount = response.total ?: 0
                response.topPrize?.let {
                    binding.prizeText.text = it
                }
                response.result?.let {
                    if (it.isNotEmpty()){
                        binding.userItemList.visibility = View.VISIBLE
                        if (offset == 1) {
                            //setting first user data
                            settingUpTopThreeUserData(it[0], false, binding.userImage1,
                                binding.pointsUser1, binding.priceUser1, null )
                            //setting second user data
                            if (it.size > 1) {
                                settingUpTopThreeUserData(
                                    it[1], true, binding.userImage2,
                                    binding.pointsUser2, binding.priceUser2, binding.userName2
                                )
                            }
                            //setting third user data
                            if (it.size > 2) {
                                settingUpTopThreeUserData(
                                    it[2], true, binding.userImage3,
                                    binding.pointsUser3, binding.priceUser3, binding.userName3
                                )
                            }
                            offset++
                            userDataList.clear()
                            userDataList = it
                            initRecyclerView()
                        }
                        else {
                            offset++
                            isLoading = false
                            userDataList.addAll(it)
                            (binding.userItemList.adapter as UserDataAdapter).userItmList = userDataList
                            binding.userItemList.adapter?.notifyDataSetChanged()
                        }
                        binding.userItemList.addOnScrollListener(scrollListener)
                    }
                    else {
                        binding.userItemList.visibility = View.GONE
                    }
                }
            }
            ON_DATA_FAILURE -> {
                binding.progressAnimationView.visibility = View.GONE
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        var previousFirstVisibleItemPosition = -1
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                val visibleItemCount = mLayoutManager?.childCount
                val totalItemCount = mLayoutManager?.itemCount
                val firstVisibleItemPosition =
                    mLayoutManager?.findFirstCompletelyVisibleItemPosition()
                firstVisibleItemPosition?.let {firstPos->
                    if(userDataList[firstPos-1].id == userId){
                        showStickyLayout(firstPos)
                    }
                }
                if (firstVisibleItemPosition == previousFirstVisibleItemPosition)
                    return
                previousFirstVisibleItemPosition = firstVisibleItemPosition!!
                if ((visibleItemCount!! + firstVisibleItemPosition) >= totalItemCount!! && firstVisibleItemPosition >= 0) {
                    if (!isLoading && totalItemCount < finalCount) {
                        isLoading = true
                        fetchData(offset)
                        recyclerView.removeOnScrollListener(this)
                    }
                }
            }
            else if (dy < 0) {
                val firstVisibleItemPosition =
                    mLayoutManager?.findFirstCompletelyVisibleItemPosition()
                firstVisibleItemPosition?.let {firstPos->
                    if(firstPos > 1){
                        if(userDataList[firstPos-1].id == userId){
                            hideStickyLayout()
                        }
                    }

                }
            }
        }
    }
    private fun hideStickyLayout(){
        binding.topLayout.visibility = View.GONE
    }

    private fun showStickyLayout(firstPos: Int){
        binding.topLayout.visibility = View.VISIBLE
        userDataList[firstPos-1].rank?.let {
            binding.rankTitle.text =it.toString()
            binding.ordinal.text = convertToOrdinal(it)
        }
        userDataList[firstPos-1].imageUrl?.let {url->
            loadUserImage(this@MainActivity, url, binding.userImage)
        }
        userDataList[firstPos-1].name?.let {
            binding.userName.text = it.toString()
        }
        userDataList[firstPos-1].points?.let {
            binding.points.text = it.toString()+" "+resources.getString(R.string.points)
        }
        userDataList[firstPos-1].price?.let {
            binding.price.text = resources.getString(R.string.rupees_symbol)+it.toString()
        }
    }

    @SuppressLint("SetTextI18n")
    fun settingUpTopThreeUserData(userData: UserData, isShowUserName: Boolean = false,
        userImageView: ImageView, userPointsTextView: TextView,
        userPriceTextView: TextView, userNameTextView: TextView?){
        loadUserImage(this, userData.imageUrl ?: "user1", userImageView)
        userPointsTextView.text =
            (userData.points.toString() + " " + resources.getString(R.string.points))
        userPriceTextView.text =
            (resources.getString(R.string.rupees_symbol)+userData.price.toString() )
        if(isShowUserName){
            userNameTextView?.text = userData.name ?: ""
        }
    }
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar("Leaderboard")
        fetchData(offset)
        binding.firstRank.text = resources.getString(R.string.first)
        binding.firstRankOrdinal.text = resources.getString(R.string.firstOrdinal)
        binding.secondRank.text = resources.getString(R.string.second)
        binding.secondRankOrdinal.text = resources.getString(R.string.secondOrdinal)
        binding.thirdRank.text = resources.getString(R.string.third)
        binding.thirdRankOrdinal.text = resources.getString(R.string.thirdOrdinal)
        initRecyclerView()
    }
    private fun initRecyclerView() {
        binding.userItemList.apply {
            mLayoutManager = LinearLayoutManager(context)
            layoutManager = mLayoutManager
            adapter = UserDataAdapter(mainViewModel, userDataList)
        }
    }

    private fun fetchData(page: Int){
        if (page == 1){
            final += (limit - 1)
        } else {
            initial +=  limit
            final += limit
        }
        mainViewModel.fetchLeaderboardData(this, initial, final)
    }
}