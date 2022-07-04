package com.vichitra.raut.uplay.ui

import android.media.MediaPlayer

object MediaPlayerClass {
    private var instance = MediaPlayer()
    private var currentIndex = -1

    fun getInstance(): MediaPlayer {
        return instance
    }

    fun setCurrentIndex(index: Int) {
        currentIndex = index
    }

    fun getCurrentIndex(): Int {
        return currentIndex
    }
}