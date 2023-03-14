package me.fakhry.githubuser.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.fakhry.githubuser.FollowAdapter
import me.fakhry.githubuser.databinding.FragmentFollowBinding
import me.fakhry.githubuser.network.ApiConfig
import me.fakhry.githubuser.network.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }

    private var position: Int = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvListFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvListFollow.addItemDecoration(itemDecoration)

        if (position == 1) {
            val client = ApiConfig.getApiService().getListFollowersOfUser(username!!)

            handleResponse(client)

        } else {
            val client = ApiConfig.getApiService().getListFollowingOfUser(username!!)

            handleResponse(client)
        }
    }

    private fun handleResponse(client: Call<List<ItemsItem>>) {
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val adapter = FollowAdapter(responseBody)
                        binding.rvListFollow.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(FollowFragment::class.java.simpleName, "onFailure: ${t.message}")
            }
        })
    }
}