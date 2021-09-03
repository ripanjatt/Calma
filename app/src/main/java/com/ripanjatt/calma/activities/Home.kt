package com.ripanjatt.calma.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.frolo.waveformseekbar.WaveformSeekBar
import com.google.android.material.tabs.TabLayout
import com.ripanjatt.calma.R
import com.ripanjatt.calma.adapters.AudioAdapter
import com.ripanjatt.calma.adapters.TabRecyclerAdapter
import com.ripanjatt.calma.databinding.ActivityHomeBinding
import com.ripanjatt.calma.services.BindingService
import com.ripanjatt.calma.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.random.Random

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val audioList = ArrayList<Audio>()
    private val searchList = ArrayList<Audio>()
    private val favList = ArrayList<Audio>()
    private val currentList = ArrayList<Audio>()
    private lateinit var audioAdapter: AudioAdapter
    private lateinit var favAdapter: AudioAdapter
    var currentItem: Audio? = null
    private var isShuffle = false
    private var isRepeat = false
    private var isFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        if(Util.checkPermissions(this)) {
            currentItem = Data.getCurrent(this)
            binding.audio = currentItem
            thread {
                audioList.clear()
                audioList.addAll(Repository.getAudio())
                currentList.apply {
                    clear()
                    addAll(audioList)
                }
                favList.apply {
                    clear()
                    addAll(Repository.getFav(this@Home))
                }
                CoroutineScope(Main).launch {
                    setUpTabs()
                    SearchListener.setSearchListener(binding.home.searchInput, audioList, searchList, binding.home.searchRecycler, this@Home)
                }
            }
        } else {
            Util.getPermissions(this)
        }
        binding.home.searchRecycler.layoutManager = LinearLayoutManager(this)
        audioAdapter = AudioAdapter(audioList, this, false)
        favAdapter = AudioAdapter(favList, this, false)
        binding.playListPopUp.playListRecycler.layoutManager = LinearLayoutManager(this)
        binding.home.playIndicator.setOnClickListener {
            Anim.showAnim(binding.expanded.root, binding.home.root, this@Home)
        }
        binding.expanded.expandedExit.setOnClickListener {
            onBackPressed()
        }
        binding.playListPopUp.playListExit.setOnClickListener {
            onBackPressed()
        }
        val array = IntArray(50)
        for(i in array.indices) {
            array[i] = Random.nextInt(1, 8)
        }
        binding.expanded.expandedSeek.setWaveform(array, true)
        binding.expanded.expandedSeek.setOnSeekBarChangeListener(object: WaveformSeekBar.OnSeekBarChangeListener {
            override fun onProgressInPercentageChanged(
                seekBar: WaveformSeekBar?,
                percent: Float,
                fromUser: Boolean
            ) {
                if(fromUser) {
                    if(currentItem != null) {
                        (currentItem?.duration?.times(percent))?.let { Player.seekTo(it.toInt()) }
                        binding.progress = (currentItem?.duration?.times(percent))?.toInt() ?: 0
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: WaveformSeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: WaveformSeekBar?) {
            }

        })
    }

    fun play(audio: Audio, list: ArrayList<Audio>, isSearch: Boolean) {
        if(isSearch) {
            binding.home.searchInput.setText("")
            val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            imm.hideSoftInputFromWindow(
                this.currentFocus?.windowToken, 0
            )
        }
        currentList.apply {
            clear()
            addAll(list)
        }
        currentItem = audio
        binding.audio = audio
        Player.play(this, audio)
        binding.isPlaying = Player.isPlaying
        isFav = favList.contains(audio)
        binding.isFav = isFav
        audioAdapter.notifyDataSetChanged()
        favAdapter.notifyDataSetChanged()
        Data.setCurrent(audio, this)
    }

    fun menu(view: View) {
        when(view.id) {
            R.id.playController,
            R.id.expandedPlay -> {
                if(Player.isPlaying) {
                    Player.pause(currentItem!!, this)
                } else {
                    if(Player.isPaused) {
                        Player.resume(currentItem!!, this)
                    } else {
                        if(currentList.size > 0) {
                            if(currentItem == null) {
                                play(currentList[Random.nextInt(currentList.size)], currentList, false)
                            } else {
                                play(currentItem!!, currentList, false)
                            }
                        }
                    }
                }
                binding.isPlaying = Player.isPlaying
            }
            R.id.expandedShuffle -> {
                isShuffle = !isShuffle
            }
            R.id.expandedRepeat -> {
                isRepeat = !isRepeat
            }
            R.id.expandedFav -> {
                isFav = !isFav
                binding.isFav = isFav
                if(!isFav) favList.remove(currentItem) else {
                    if(currentItem != null) {
                        favList.add(currentItem!!)
                    }
                }
                favList.sortBy { it.name }
                Data.setFav(favList, this)
            }
            R.id.playNext,
            R.id.expandedNext -> {
                if(currentList.size > 0) {
                    if(isShuffle) play(currentList[Random.nextInt(currentList.size)], ArrayList(currentList), false)
                    else {
                        if (currentList.indexOf(currentItem) == currentList.size - 1) {
                            play(currentList[0], ArrayList(currentList)
                                , false)
                        }
                        else play(currentList[currentList.indexOf(currentItem) + 1], ArrayList(currentList), false)
                    }
                } else {
                    currentList.addAll(audioList)
                }
            }
            R.id.expandedPrev -> {
                if(currentList.size > 0) {
                    if(isShuffle) play(currentList[Random.nextInt(currentList.size)], ArrayList(currentList), false)
                    else {
                        if (currentList.indexOf(currentItem) == 0) play(
                            currentList[currentList.size - 1],
                            ArrayList(currentList)
                            , false)
                        else play(currentList[currentList.indexOf(currentItem) - 1], ArrayList(currentList), false)
                    }
                } else {
                    currentList.addAll(audioList)
                }
            }
        }
        Util.setPreferences(isShuffle, isRepeat, this)
        binding.apply {
            isShuffle = Util.getShuffle(this@Home)
            isRepeat = Util.getRepeat(this@Home)
        }
    }

    fun showPlayList(name: String, uri: String, id: Long, list: ArrayList<Audio>) {
        binding.apply {
            albumID = id
            playListName = name
            contentUri = uri
            size = if(list.size > 1) "${list.size} songs" else "1 song"
        }
        Anim.showAnim(binding.playListPopUp.root, binding.home.root, this)
        val temp = AudioAdapter(list, this, false)
        binding.playListPopUp.playListRecycler.adapter = temp
        binding.playListPopUp.playListPlay.setOnClickListener {
            if(isShuffle) play(list[Random.nextInt(list.size)], list, false)
            else play(list[0], list, false)
            temp.notifyDataSetChanged()
        }
    }

    fun showArtistList(name: String, list: ArrayList<Audio>) {
        binding.apply {
            playListName = name
            albumID = null
            contentUri = null
            size = if(list.size > 1) "${list.size} songs" else "1 song"
        }
        Anim.showAnim(binding.playListPopUp.root, binding.home.root, this)
        val temp = AudioAdapter(list, this, false)
        binding.playListPopUp.playListRecycler.adapter = temp
        binding.playListPopUp.playListPlay.setOnClickListener {
            if(isShuffle) play(list[Random.nextInt(list.size)], list, false)
            else play(list[0], list, false)
            temp.notifyDataSetChanged()
        }
    }

    fun setVisibility(visibility: Int) {
        binding.home.tabLayout.visibility = visibility
        binding.home.tabPager.visibility = visibility
        binding.home.playIndicator.visibility = visibility
    }

    private fun setUpTabs() {
        val tabs = arrayListOf(
            LayoutUtil.getFav(layoutInflater, favAdapter, this),
            LayoutUtil.getAll(layoutInflater, audioAdapter, this),
            LayoutUtil.getAlbum(layoutInflater, this),
            LayoutUtil.getArtist(layoutInflater, this)
        )
        val adapter = TabRecyclerAdapter(tabs)
        binding.home.apply {
            tabPager.adapter = adapter
            tabPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.home.tabLayout.selectTab(binding.home.tabLayout.getTabAt(position))
                }
            })
            tabPager.setCurrentItem(1, true)
            tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tabPager.setCurrentItem(tabLayout.selectedTabPosition, true)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
        Player.setProgressListener(object: Player.ProgressListener {
            override fun progress(progress: Int) {
                binding.expanded.expandedSeek.setVal(((progress * 100) / currentItem?.duration!!).toInt())
                binding.progress = progress
            }

            override fun onComplete() {
                if(currentList.size == 0) {
                    currentList.addAll(audioList)
                }
                if(Player.isPlaying) {
                    if(isRepeat) {
                        play(currentItem!!, currentList, false)
                    } else {
                        menu(binding.home.playNext)
                    }
                }
            }
        })
        BindingService.setChangeListener(object: BindingService.ChangeListener {
            override fun onReceive(action: String) {
                when(action) {
                    "play" -> {
                        menu(binding.home.playController)
                    }
                    "prev" -> {
                        menu(binding.expanded.expandedPrev)
                    }
                    "next" -> {
                        menu(binding.expanded.expandedNext)
                    }
                }
            }
        })
        isShuffle = Util.getShuffle(this)
        isRepeat = Util.getRepeat(this)
        binding.isShuffle = isShuffle
        binding.isRepeat = isRepeat
        binding.isFav = favList.contains(currentItem)
        binding.isPlaying = Player.isPlaying
        binding.progress = 0
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 8192) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Repository.loadFiles(this)
                thread {
                    audioList.apply {
                        clear()
                        addAll(Repository.getAudio())
                    }
                    currentList.apply {
                        clear()
                        addAll(audioList)
                    }
                    currentItem = Data.getCurrent(this)
                    binding.audio = currentItem
                    favList.apply {
                        clear()
                        addAll(Repository.getFav(this@Home))
                    }
                    CoroutineScope(Main).launch {
                        setUpTabs()
                        SearchListener.setSearchListener(binding.home.searchInput, audioList, searchList, binding.home.searchRecycler, this@Home)
                    }
                }
            } else {
                Util.permissionDialog(this)
            }
        }
    }

    override fun onBackPressed() {
        when {
            (binding.home.searchRecycler.visibility == View.VISIBLE) -> {
                binding.home.searchInput.setText("")
            }
            (binding.expanded.root.visibility == View.VISIBLE) -> {
                Anim.hideAnim(binding.expanded.root, binding.home.root, this)
            }
            (binding.playListPopUp.root.visibility == View.VISIBLE) -> {
                Anim.hideAnim(binding.playListPopUp.root, binding.home.root, this)
            }
            else -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Exit?")
                    setNegativeButton("No", null)
                    setPositiveButton("Yes") { _, _ ->
                        super.onBackPressed()
                    }
                }.show()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        Repository.loadFiles(this)
        thread {
            audioList.apply {
                clear()
                addAll(Repository.getAudio())
            }
            currentList.apply {
                clear()
                addAll(audioList)
            }
            currentItem = Data.getCurrent(this)
            binding.audio = currentItem
            favList.apply {
                clear()
                addAll(Repository.getFav(this@Home))
            }
            CoroutineScope(Main).launch {
                setUpTabs()
                SearchListener.setSearchListener(binding.home.searchInput, audioList, searchList, binding.home.searchRecycler, this@Home)
            }
        }
    }
}