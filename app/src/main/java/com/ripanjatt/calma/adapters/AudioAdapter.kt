package com.ripanjatt.calma.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ripanjatt.calma.R
import com.ripanjatt.calma.activities.Home
import com.ripanjatt.calma.databinding.AudioListItemBinding
import com.ripanjatt.calma.util.Audio
import com.ripanjatt.calma.util.Repository

class AudioAdapter(private val list: ArrayList<Audio>, private val home: Home, private val isSearch: Boolean
) : RecyclerView.Adapter<AudioAdapter.AudioHolder>() {

    class AudioHolder(val binding: AudioListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {
        return AudioHolder(
            AudioListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AudioHolder, position: Int) {
        if(home.currentItem == list[position]) {
            holder.binding.rootCont.setBackgroundColor(home.resources.getColor(R.color.purple_search, null))
        } else {
            holder.binding.rootCont.setBackgroundColor(Color.TRANSPARENT)
        }
        holder.binding.audioArt.setImageResource(R.mipmap.music_icon)
        holder.binding.audioName.text = list[position].name
        holder.binding.audioInfo.text = ("${list[position].artist} | ${list[position].album}")
        Repository.setBitmap(holder.binding.audioArt, list[position].albumID)
        holder.binding.rootCont.setOnClickListener {
            holder.binding.rootCont.setBackgroundColor(home.resources.getColor(R.color.purple_search, null))
            if(isSearch) {
                home.play(list[position], Repository.getAudio(), isSearch)
            } else {
                home.play(list[position], list, isSearch)
            }
            this@AudioAdapter.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}