package com.ripanjatt.calma.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ripanjatt.calma.R
import com.ripanjatt.calma.activities.Home
import com.ripanjatt.calma.databinding.AudioListItemBinding
import com.ripanjatt.calma.util.Audio
import com.ripanjatt.calma.util.Repository
import java.util.*
import kotlin.collections.ArrayList

class AlbumAdapter(private val map: SortedMap<String?, MutableList<Audio>>, private val home: Home)
    : RecyclerView.Adapter<AlbumAdapter.AlbumHolder>()
{
    private val keys = ArrayList<String>()

    init {
        map.keys.forEach {
            keys.add(it.toString())
        }
    }

    class AlbumHolder(val binding: AudioListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        return AlbumHolder(
            AudioListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        val uri = map[keys[position]]?.get(0)?.uri
        val albumID = map[keys[position]]?.get(0)?.albumID.toString().toLong()
        holder.binding.audioArt.setImageResource(R.mipmap.music_icon)
        holder.binding.audioName.text = keys[position]
        holder.binding.audioInfo.text = if(map[keys[position]]?.size!! > 1)
                    ("${map[keys[position]]?.size} songs")
                else ("${map[keys[position]]?.size} song")
        Repository.setBitmap(holder.binding.audioArt, albumID)
        holder.binding.rootCont.setOnClickListener {
            home.showPlayList(keys[position], uri.toString(), albumID,
                map[keys[position]] as ArrayList<Audio>
            )
        }
    }

    override fun getItemCount(): Int {
        return map.size
    }
}