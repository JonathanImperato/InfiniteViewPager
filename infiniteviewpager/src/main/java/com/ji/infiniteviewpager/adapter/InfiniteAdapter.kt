package com.ji.infiniteviewpager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ji.infiniteviewpager.onBind
import com.ji.infiniteviewpager.onDiff


class InfiniteAdapter<T>(
    @LayoutRes private val id: Int = android.R.layout.activity_list_item,
    var onBind: onBind<T> = { _, _ -> },
    var onDiff: onDiff<T> = { _, _ -> true },
) :
    ListAdapter<T, InfiniteRecyclerViewHolder<T>>(
        object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) = onDiff(oldItem, newItem)
            override fun areContentsTheSame(oldItem: T, newItem: T) = onDiff(oldItem, newItem)
        }
    ) {
    
    override fun submitList(list: MutableList<T>?) {
        if (list == null) return
        val last = list.last()
        val first = list.first()
        list.add(0, last)
        list.add(list.size, first)
        super.submitList(list)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InfiniteRecyclerViewHolder(parent, id, onBind)
    
    override fun onBindViewHolder(holder: InfiniteRecyclerViewHolder<T>, position: Int) = holder.bind(getItem(position))
    
}

class InfiniteRecyclerViewHolder<T>(parent: ViewGroup, @LayoutRes id: Int, private val onBind: onBind<T>) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(id, parent, false)) {
    fun bind(data: T) = onBind(data, itemView)
}