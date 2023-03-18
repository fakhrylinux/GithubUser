package me.fakhry.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import me.fakhry.githubuser.ListUserAdapter
import me.fakhry.githubuser.R
import me.fakhry.githubuser.data.network.response.ItemsItem
import me.fakhry.githubuser.databinding.ActivityMainBinding
import me.fakhry.githubuser.ui.detail.UserDetailActivity
import me.fakhry.githubuser.ui.favorite.FavoriteActivity
import me.fakhry.githubuser.util.showLoading

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvListUser.layoutManager = layoutManager
        val itemDecoration = MaterialDividerItemDecoration(this, layoutManager.orientation)
        binding.rvListUser.addItemDecoration(itemDecoration)

        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.listUsers.observe(this) { listUsers ->
            setUpUserList(listUsers)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.showLoading(isLoading)
        }

        mainViewModel.isError.observe(this) { isError ->
            if (isError) {
                binding.tvErrorMessage.visibility = View.VISIBLE
            } else {
                binding.tvErrorMessage.visibility = View.GONE
            }
        }

        mainViewModel.respondMessage.observe(this) { message ->
            binding.tvErrorMessage.text = message
        }
    }

    private fun setUpUserList(items: List<ItemsItem>) {
        binding.progressBar.showLoading(false)
        val adapter = ListUserAdapter(items)
        binding.rvListUser.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intentDetail = Intent(this@MainActivity, UserDetailActivity::class.java)
                intentDetail.putExtra(UserDetailActivity.EXTRA_USER, data.login)
                startActivity(intentDetail)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}