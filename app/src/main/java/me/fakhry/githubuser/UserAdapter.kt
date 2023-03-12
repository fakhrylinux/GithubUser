package me.fakhry.githubuser

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import me.fakhry.githubuser.databinding.RowItemBinding
import me.fakhry.githubuser.model.ItemsItem
import me.fakhry.githubuser.ui.UserDetailActivity

class UserAdapter(private val listUser: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(var binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = listUser[position].login
        val photo = listUser[position].avatarUrl
        holder.binding.tvName.text = name
        holder.binding.ivPhoto.load(photo)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, UserDetailActivity::class.java)
            holder.itemView.context.startActivity(intentDetail)
        }
    }
}