package com.ripanjatt.calma.util

import android.net.Uri
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.frolo.waveformseekbar.WaveformSeekBar
import com.ripanjatt.calma.R

@BindingAdapter("contentUri", "albumID")
fun ImageView.contentUri(contentUri: String?, albumID: Long?) {
    if(contentUri != null && albumID != null) {
        this.setImageBitmap(Util.getBitmap(context, Uri.parse(contentUri), albumID))
    } else {
        this.setImageResource(R.drawable.ic_baseline_person_24)
    }
}

@BindingAdapter("android:setDuration")
fun TextView.setDuration(long: Long) {
    this.text = Util.getTime(long)
}

@BindingAdapter("android:setDrawableShuffle")
fun ImageButton.setDrawableShuffle(isShuffle: Boolean) {
    if(isShuffle) {
        this.setImageResource(R.drawable.ic_baseline_shuffle_active_24)
        this.setBackgroundResource(R.drawable.button_back_active)
    } else {
        this.setImageResource(R.drawable.ic_baseline_shuffle_24)
        this.setBackgroundResource(R.drawable.button_back)
    }
}

@BindingAdapter("android:setDrawableRepeat")
fun ImageButton.setDrawableRepeat(isRepeat: Boolean) {
    if(isRepeat) {
        this.setImageResource(R.drawable.ic_baseline_repeat_one_active_24)
        this.setBackgroundResource(R.drawable.button_back_active)
    } else {
        this.setImageResource(R.drawable.ic_baseline_repeat_one_24)
        this.setBackgroundResource(R.drawable.button_back)
    }
}

@BindingAdapter("android:setDrawableFav")
fun ImageButton.setDrawableFav(isFav: Boolean) {
    if(isFav) {
        this.setImageResource(R.drawable.ic_baseline_favorite_active_24)
        this.setBackgroundResource(R.drawable.button_back_active)
    } else {
        this.setImageResource(R.drawable.ic_baseline_favorite_24)
        this.setBackgroundResource(R.drawable.button_back)
    }
}

@BindingAdapter("android:setDrawablePlay")
fun ImageButton.setDrawablePlay(isPlaying: Boolean) {
    if(isPlaying) {
        this.setImageResource(R.drawable.ic_baseline_pause_24)
    } else {
        this.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }
}

@BindingAdapter("android:setDrawablePlayExp")
fun ImageButton.setDrawablePlayExp(isPlaying: Boolean) {
    if(isPlaying) {
        this.setImageResource(R.drawable.ic_baseline_pause_48)
    } else {
        this.setImageResource(R.drawable.ic_baseline_play_arrow_48)
    }
}

@BindingAdapter("android:setMaxVal")
fun ProgressBar.setMaxVal(long: Long) {
    this.max = long.toString().toInt()
}

@BindingAdapter("android:setMaxVal")
fun WaveformSeekBar.setVal(int: Int) {
    this.setProgressInPercentage(int.toFloat() / 100)
}