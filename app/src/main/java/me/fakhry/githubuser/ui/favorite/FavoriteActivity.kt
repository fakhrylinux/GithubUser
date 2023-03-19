package me.fakhry.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import me.fakhry.githubuser.R
import me.fakhry.githubuser.data.network.response.ItemsItem
import me.fakhry.githubuser.databinding.ActivityFavoriteBinding
import me.fakhry.githubuser.ui.ListUserAdapter
import me.fakhry.githubuser.ui.ViewModelFactory
import me.fakhry.githubuser.ui.detail.UserDetailActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var adapter: ListUserAdapter = ListUserAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)

        supportActionBar?.title = getString(R.string.favorite_user)

        val layoutManager = LinearLayoutManager(this)
        binding.rvListFavorite.layoutManager = layoutManager
        val itemDecoration = MaterialDividerItemDecoration(this, layoutManager.orientation)
        binding.rvListFavorite.addItemDecoration(itemDecoration)

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getFavoriteUsers()
    }

    private fun observeViewModel() {
        favoriteViewModel.listFavorite.observe(this) { listFavorite ->
            if (listFavorite.isEmpty()) {
                binding.tvErrorMessage.visibility = View.VISIBLE
                binding.tvErrorMessage.text = getString(R.string.you_dont_have_favorite_user)
            }
            val items = arrayListOf<ItemsItem>()
            listFavorite.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            setUpUserList(items)
        }

        favoriteViewModel.isError.observe(this) { isError ->
            binding.tvErrorMessage.visibility = View.VISIBLE
        }

        favoriteViewModel.respondMessage.observe(this) { respondMessage ->
            binding.tvErrorMessage.text = respondMessage
        }
    }

    private fun setUpUserList(items: List<ItemsItem>) {
        adapter = ListUserAdapter(items)
        binding.rvListFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intentDetail = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                intentDetail.putExtra(UserDetailActivity.EXTRA_USER, data.login)
                startActivity(intentDetail)
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}