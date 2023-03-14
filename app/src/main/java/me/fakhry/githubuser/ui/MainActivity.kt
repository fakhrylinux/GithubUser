package me.fakhry.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
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

        val layouManager = LinearLayoutManager(this)
        binding.rvListUser.layoutManager = layouManager
        val itemDecoration = DividerItemDecoration(this, layouManager.orientation)
        binding.rvListUser.addItemDecoration(itemDecoration)

        mainViewModel.listUsers.observe(this) { listUsers ->
            setUserData(listUsers)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.showLoading(isLoading)
        }
    }

    private fun setUserData(items: List<ItemsItem>) {
        binding.progressBar.showLoading(false)
        val adapter = ListUserAdapter(items)
        binding.rvListUser.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.findUser(query!!)
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }
}