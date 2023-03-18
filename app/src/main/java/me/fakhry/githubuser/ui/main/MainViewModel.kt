package me.fakhry.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.fakhry.githubuser.data.network.response.ItemsItem
import me.fakhry.githubuser.data.network.response.SearchUserResponse
import me.fakhry.githubuser.data.network.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _respondMessage = MutableLiveData<String>()
    val respondMessage: LiveData<String> = _respondMessage

    init {
        findUser(INITIAL_QUERY)
    }

    fun findUser(keyword: String) {
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().getUsersByQuery(keyword)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.items.isEmpty()) {
                            _isError.value = true
                            _respondMessage.value = "No Data Found"
                        }
                        _listUsers.value = responseBody.items
                    }
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _respondMessage.value = "${t.message}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val INITIAL_QUERY = "android"
    }
}