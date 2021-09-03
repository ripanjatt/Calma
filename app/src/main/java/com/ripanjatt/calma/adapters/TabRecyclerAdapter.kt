package com.ripanjatt.calma.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ripanjatt.calma.databinding.PagerContainerBinding

class TabRecyclerAdapter(private val list: ArrayList<View>) : RecyclerView.Adapter<TabRecyclerAdapter.RecyclerHolder>() {

    class RecyclerHolder(val binding: PagerContainerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder(
            PagerContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.binding.frame.removeAllViews()
        holder.binding.frame.addView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}