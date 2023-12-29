package com.example.examsuitmedia.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.examsuitmedia.R
import com.example.examsuitmedia.databinding.ItemListBinding
import com.example.examsuitmedia.retrofit.DataItem
import com.example.examsuitmedia.ui.SecondScreenActivity

class ListAdapter(private val userList: List<DataItem>, private val itemClickListener: ((DataItem) -> Unit)?, ): RecyclerView.Adapter<ListAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):UserViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: DataItem) {
            binding.tvUser.text = "${user.firstName} ${user.lastName}"
            binding.tvEmail.text = user.email

            // Load image using Glide or any other image loading library
            Glide.with(binding.root.context)
                .load(user.avatar)
                .into(binding.ivAvatar)

            binding.cvUser.setOnClickListener {
                itemClickListener?.invoke(user)

                // Menambahkan kode untuk mengirim data pengguna yang dipilih ke SecondScreenActivity
                val intent = Intent(binding.root.context, SecondScreenActivity::class.java)
                intent.putExtra("SELECTED_USER_NAME", "${user.firstName} ${user.lastName}")
                binding.root.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}
