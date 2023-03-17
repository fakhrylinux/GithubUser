package me.fakhry.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import me.fakhry.githubuser.databinding.ItemFollowBinding
import me.fakhry.githubuser.data.network.response.ItemsItem

class FollowAdapter(private val listFollow: List<ItemsItem>) :
    RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemFollowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val avatar = listFollow[position].avatarUrl
        val login = listFollow[position].login
        holder.binding.ivPhoto.load(avatar)
        holder.binding.tvLogin.text = login
    }

    override fun getItemCount(): Int = listFollow.size
}