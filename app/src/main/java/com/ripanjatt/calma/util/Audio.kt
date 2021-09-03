package com.ripanjatt.calma.util

import java.io.Serializable

data class Audio(val uri: String,
                 val name: String,
                 val duration: Long,
                 val size: Int,
                 val album: String,
                 val artist: String,
                 val albumID: Long
) : Serializable
