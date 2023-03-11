package me.fakhry.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.fakhry.githubuser.databinding.RowItemBinding

class UserAdapter(private val listUser: List<String>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(var binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = listUser[position]
        holder.binding.tvName.text = name
    }
}