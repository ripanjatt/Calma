package com.ripanjatt.calma.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ripanjatt.calma.activities.Home
import com.ripanjatt.calma.adapters.AlbumAdapter
import com.ripanjatt.calma.adapters.ArtistAdapter
import com.ripanjatt.calma.adapters.AudioAdapter
import com.ripanjatt.calma.databinding.FragmentAlbumBinding
import com.ripanjatt.calma.databinding.FragmentAllBinding
import com.ripanjatt.calma.databinding.FragmentArtistBinding
import com.ripanjatt.calma.databinding.FragmentFavBinding

object LayoutUtil {

    fun getFav(layoutInflater: LayoutInflater, favAdapter: AudioAdapter, context: Context): View {
        val binding = FragmentFavBinding.inflate(layoutInflater)
        binding.favRecycler.layoutManager = LinearLayoutManager(context)
        binding.favRecycler.adapter = favAdapter
        return binding.root
    }

    fun getAll(layoutInflater: LayoutInflater, audioAdapter: AudioAdapter, context: Context): View {
        val binding = FragmentAllBinding.inflate(layoutInflater)
        binding.audioRecycler.layoutManager = LinearLayoutManager(context)
        binding.audioRecycler.adapter = audioAdapter
        return binding.root
    }

    fun getAlbum(layoutInflater: LayoutInflater, home: Home): View {
        val binding = FragmentAlbumBinding.inflate(layoutInflater)
        binding.albumRecycler.layoutManager = LinearLayoutManager(home.applicationContext)
        binding.albumRecycler.adapter = AlbumAdapter(Repository.getAlbums(), home)
        return binding.root
    }

    fun getArtist(layoutInflater: LayoutInflater, home: Home): View {
        val binding = FragmentArtistBinding.inflate(layoutInflater)
        binding.artistRecycler.layoutManager = LinearLayoutManager(home.applicationContext)
        binding.artistRecycler.adapter = ArtistAdapter(Repository.getArtists(), home)
        return binding.root
    }
}