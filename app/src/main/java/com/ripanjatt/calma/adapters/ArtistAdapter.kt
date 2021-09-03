package com.ripanjatt.calma.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ripanjatt.calma.R
import com.ripanjatt.calma.activities.Home
import com.ripanjatt.calma.databinding.AudioListItemBinding
import com.ripanjatt.calma.util.Audio
import java.util.*
import kotlin.collections.ArrayList

class ArtistAdapter(private val map: SortedMap<String?, MutableList<Audio>>, private val home: Home)
    : RecyclerView.Adapter<ArtistAdapter.ArtistHolder>() {

    private val keys = ArrayList<String>()

    init {
        map.keys.forEach {
            keys.add(it.toString())
        }
    }

    class ArtistHolder(val binding: AudioListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHolder {
        return ArtistHolder(
            AudioListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArtistHolder, position: Int) {
        holder.binding.audioArt.setImageResource(R.drawable.ic_baseline_person_24)
        holder.binding.audioName.text = keys[position]
        holder.binding.audioInfo.text = if(map[keys[position]]?.size!! > 1)
            ("${map[keys[position]]?.size} songs")
        else ("${map[keys[position]]?.size} song")
        holder.binding.rootCont.setOnClickListener {
            home.showArtistList(keys[position], map[keys[position]] as ArrayList<Audio>)
        }
    }

    override fun getItemCount(): Int {
        return map.size
    }
}