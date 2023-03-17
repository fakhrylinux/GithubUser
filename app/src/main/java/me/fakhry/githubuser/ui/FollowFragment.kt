package me.fakhry.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.fakhry.githubuser.FollowAdapter
import me.fakhry.githubuser.ui.detail.UserDetailViewModel
import me.fakhry.githubuser.databinding.FragmentFollowBinding
import me.fakhry.githubuser.util.showLoading

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding

    private var position: Int = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val userDetailViewModel: UserDetailViewModel by viewModels { factory }

        val layoutManager = LinearLayoutManager(context)
        binding?.rvListFollow?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding?.rvListFollow?.addItemDecoration(itemDecoration)

        userDetailViewModel.setUserFollow(username ?: "", position)

        userDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding?.pbFollow?.showLoading(isLoading)
        }

        userDetailViewModel.userFollow.observe(viewLifecycleOwner) { userFollow ->
            val adapter = FollowAdapter(userFollow)
            binding?.rvListFollow?.adapter = adapter
        }
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}