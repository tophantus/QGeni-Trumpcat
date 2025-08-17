package com.example.qgeni.utils

import android.media.MediaPlayer

class AudioPlayer(private val audioUrl: String, private val onCompletion: () -> Unit) {
    private lateinit var mediaPlayer: MediaPlayer
    private var isMuted: Boolean = false

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl)
            prepare()
            setOnCompletionListener {
                onCompletion()
            }
        }
    }

    fun play() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun reset() {
        mediaPlayer.reset()
        initializePlayer()
    }

    fun moveTo(timeInMillis: Int) {
        mediaPlayer.seekTo(timeInMillis)
    }

    fun setSpeed(speed: Float) {
        mediaPlayer.playbackParams.speed = speed

    }

    fun mute(isMute: Boolean) {
        isMuted = isMute
        mediaPlayer.setVolume(if (isMuted) 0f else 1f, if (isMuted) 0f else 1f)
    }

    fun release() {
        mediaPlayer.release()
    }

    fun getDurationSecond(): Float {
        return mediaPlayer.duration.toFloat() / 1000
    }

    fun getPositionSecond(): Float {
        return mediaPlayer.currentPosition.toFloat() / 1000
    }
}
