package me.fakhry.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.fakhry.githubuser.ListUserAdapter
import me.fakhry.githubuser.MainViewModel
import me.fakhry.githubuser.R
import me.fakhry.githubuser.databinding.ActivityMainBinding
import me.fakhry.githubuser.network.response.ItemsItem
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
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListUser.addItemDecoration(itemDecoration)

        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.listUsers.observe(this) { listUsers ->
            setUserData(listUsers)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.showLoading(isLoading)
        }
    }

    private fun setUserData(items: List<ItemsItem>) {
        binding.progressBar.showLoading(false)
        if (items.isEmpty()) {
            binding.tvErrorMessage.visibility = View.VISIBLE
        }
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
                binding.tvErrorMessage.visibility = View.GONE
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
}