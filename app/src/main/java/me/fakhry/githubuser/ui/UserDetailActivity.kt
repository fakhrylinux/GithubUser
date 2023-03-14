package me.fakhry.githubuser.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import me.fakhry.githubuser.R
import me.fakhry.githubuser.SectionsPagerAdapter
import me.fakhry.githubuser.UserDetailViewModel
import me.fakhry.githubuser.databinding.ActivityUserDetailBinding
import me.fakhry.githubuser.network.response.GetUserResponse
import me.fakhry.githubuser.util.showLoading

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val userDetailViewModel: UserDetailViewModel by viewModels()

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

        userDetailViewModel.setUserDetail(intent.getStringExtra(EXTRA_USER)!!)

        userDetailViewModel.userDetail.observe(this) { userDetail ->
            setUserView(userDetail!!)
        }

        userDetailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.showLoading(isLoading)
        }
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