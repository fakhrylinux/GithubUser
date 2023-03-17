package me.fakhry.githubuser.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import me.fakhry.githubuser.R
import me.fakhry.githubuser.SectionsPagerAdapter
import me.fakhry.githubuser.UserDetailViewModel
import me.fakhry.githubuser.data.local.entity.FavoriteEntity
import me.fakhry.githubuser.data.network.response.GetUserResponse
import me.fakhry.githubuser.data.network.response.ItemsItem
import me.fakhry.githubuser.databinding.ActivityUserDetailBinding
import me.fakhry.githubuser.util.showLoading

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var menuFav: Menu
//    private var favorite: FavoriteEntity? = null
    private var user: ItemsItem? = null
    private lateinit var userDetailViewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDetailViewModel = obtainViewModel(this@UserDetailActivity)

//        supportActionBar?.hide()

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = intent.getStringExtra(EXTRA_USER) ?: ""
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        userDetailViewModel.setUserDetail(intent.getStringExtra(EXTRA_USER) ?: "")

        userDetailViewModel.userDetail.observe(this) { userDetail ->
            populateDetailView(userDetail)
        }

        userDetailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.showLoading(isLoading)
        }
    }

    private fun populateDetailView(responseBody: GetUserResponse) {
        binding.apply {
            ivAvatar.load(responseBody.avatarUrl)
            tvFullName.text = responseBody.name
            tvLogin.text = responseBody.login
            tvBio.text = responseBody.bio
            tvFollowers.text = getString(R.string._999_followers, responseBody.followers)
            tvFollowing.text = getString(R.string._999_following, responseBody.following)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)
        menuFav = menu

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
//                favorite?.let { favorite ->
//                }

                userDetailViewModel.saveFavorite(true)

                Toast.makeText(this, "Add to favorite", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserDetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[UserDetailViewModel::class.java]
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "UserDetailActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}