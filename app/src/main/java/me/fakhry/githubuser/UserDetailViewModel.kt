package me.fakhry.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.fakhry.githubuser.network.ApiConfig
import me.fakhry.githubuser.network.response.GetUserResponse
import me.fakhry.githubuser.network.response.ItemsItem
import me.fakhry.githubuser.ui.FollowFragment
import me.fakhry.githubuser.ui.UserDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {

    private val _userDetail = MutableLiveData<GetUserResponse>()
    val userDetail: MutableLiveData<GetUserResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userFollow = MutableLiveData<List<ItemsItem>>()
    val userFollow: MutableLiveData<List<ItemsItem>> = _userFollow

    fun setUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _userDetail.value = response.body()
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.e(UserDetailActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setUserFollow(username: String, position: Int) {
        if (position == 0) {
            val client = ApiConfig.getApiService().getListFollowersOfUser(username)
            getDataFollow(client)
        } else {
            val client = ApiConfig.getApiService().getListFollowingOfUser(username)
            getDataFollow(client)
        }
    }

    private fun getDataFollow(client: Call<List<ItemsItem>>) {
        _isLoading.value = true
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _userFollow.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(FollowFragment::class.java.simpleName, "onFailure: ${t.message}")
            }
        })
    }
}