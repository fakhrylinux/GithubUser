package me.fakhry.githubuser.ui

import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import me.fakhry.githubuser.R
import me.fakhry.githubuser.SectionsPagerAdapter
import me.fakhry.githubuser.databinding.ActivityUserDetailBinding
import me.fakhry.githubuser.network.ApiConfig
import me.fakhry.githubuser.network.response.GetUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "UserDetailActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = intent.getStringExtra(EXTRA_USER)!!
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        getUser()
    }

    private fun getUser() {
        val client = ApiConfig.getApiService().getUser(intent.getStringExtra(EXTRA_USER)!!)
        client.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserView(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserView(responseBody: GetUserResponse) {
        binding.apply {
            ivAvatar.load(responseBody.avatarUrl)
            tvFullName.text = responseBody.name
            tvLogin.text = responseBody.login
            tvEmail.text = responseBody.email
            tvFollowers.text = getString(R.string._999_followers, responseBody.followers)
            tvFollowing.text = getString(R.string._999_following, responseBody.following)
        }
    }
}