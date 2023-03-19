package me.fakhry.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.fakhry.githubuser.data.local.entity.FavoriteEntity
import me.fakhry.githubuser.data.network.response.GetUserResponse
import me.fakhry.githubuser.data.network.response.ItemsItem
import me.fakhry.githubuser.data.network.retrofit.ApiConfig
import me.fakhry.githubuser.data.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    private val _userDetail = MutableLiveData<GetUserResponse>()
    val userDetail: LiveData<GetUserResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userFollow = MutableLiveData<List<ItemsItem>>()
    val userFollow: LiveData<List<ItemsItem>> = _userFollow

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getUserDetail(username: String) {
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

    fun isFavorite(username: String) {
        viewModelScope.launch {
            _isFavorite.value = favoriteRepository.isFavorite(username)
        }
    }

    fun saveFavorite(isFavorite: Boolean) {
        val username = _userDetail.value?.login ?: ""
        val avatarUrl = _userDetail.value?.avatarUrl ?: ""
        val favoriteEntity = FavoriteEntity(
            username = username,
            avatarUrl = avatarUrl,
            isFavorite = isFavorite
        )
        viewModelScope.launch {
            favoriteRepository.setFavorite(favoriteEntity, true)
        }
        _isFavorite.value = true
    }

    fun deleteFavorite(username: String) {
        val avatarUrl = _userDetail.value?.avatarUrl ?: ""
        val favoriteEntity = FavoriteEntity(
            username = username,
            avatarUrl = avatarUrl,
            isFavorite = true
        )
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(favoriteEntity)
        }
        _isFavorite.value = false
    }
}