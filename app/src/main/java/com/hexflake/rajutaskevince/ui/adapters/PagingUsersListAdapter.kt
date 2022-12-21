package com.hexflake.rajutaskevince.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.hexflake.rajutaskevince.database.UserModel
import com.hexflake.rajutaskevince.databinding.RowUserlistBinding
import com.hexflake.rajutaskevince.utils.click

class PagingUsersListAdapter (val onUserClick:(UserModel)->Unit): PagingDataAdapter<UserModel, PagingUsersListAdapter.UserListViewHolder>(DiffCallback) {

    class UserListViewHolder(val binding: RowUserlistBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserListViewHolder(
            RowUserlistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val item = getItem(position)?.let { modelData ->
            holder.binding.apply {

                //set data to xml with model
                model=modelData

                // when user click we are going back with callback and data
                root click { modelData.run(onUserClick::invoke) }
            }

        }
    }

    object DiffCallback : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(old: UserModel, new: UserModel) = old == new
        override fun areContentsTheSame(old: UserModel, new: UserModel) = old.id == new.id
    }

}