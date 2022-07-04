package com.vichitra.raut.uplay.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.vichitra.raut.uplay.R
import com.vichitra.raut.uplay.data.AudioModel
import com.vichitra.raut.uplay.databinding.ActivityMediaPlayerBinding
import java.util.concurrent.TimeUnit


class MediaPlayerActivity : AppCompatActivity() {

    private var songList: ArrayList<AudioModel> = arrayListOf()
    var binding: ActivityMediaPlayerBinding? = null
    var mediaPlayer: MediaPlayer? = null
    var x : Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        mediaPlayer = MediaPlayerClass.getInstance()

        getDataFromIntent()

        setupUI()

        initClick()

        this@MediaPlayerActivity.runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    binding?.seekBar?.progress = mediaPlayer!!.currentPosition
                    binding?.currentTime?.text = convertToMMSS(mediaPlayer!!.currentPosition.toString() + "")
                    if (mediaPlayer!!.isPlaying) {
                        binding?.pausePlay?.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        binding?.musicIconBig?.rotation = x++
                    } else {
                        binding?.pausePlay?.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        binding?.musicIconBig?.rotation = 0F
                    }
                }
                Handler().postDelayed(this, 10)
            }
        })

    }

    private fun initClick() {
        binding?.previous?.setOnClickListener { previousSong() }
        binding?.next?.setOnClickListener { nextSong() }
        binding?.pausePlay?.setOnClickListener { pausePlay() }
        binding?.seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (mediaPlayer != null && p2){
                    mediaPlayer?.seekTo(p1)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        playMusic()
    }

    private fun playMusic() {
        mediaPlayer?.reset()
        try {
            mediaPlayer?.setDataSource(songList[MediaPlayerClass.getCurrentIndex()].path)
            mediaPlayer?.prepare()
            binding?.seekBar?.progress = 0
            binding?.seekBar?.max = mediaPlayer?.duration!!
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setupUI()
    }

    private fun pausePlay() {

        if (mediaPlayer != null) {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer?.pause()
            } else {
                mediaPlayer?.start()
            }
        }
    }

    private fun nextSong() {
        if (MediaPlayerClass.getCurrentIndex() == songList.size - 1) {
            return
        }
        MediaPlayerClass.setCurrentIndex(MediaPlayerClass.getCurrentIndex() + 1)
        mediaPlayer?.reset()
        playMusic()
    }

    private fun previousSong() {
        if (MediaPlayerClass.getCurrentIndex() == 0) {
            return
        }
        MediaPlayerClass.setCurrentIndex(MediaPlayerClass.getCurrentIndex() - 1)
        mediaPlayer?.reset()
        playMusic()
    }

    private fun setupUI() {
        if (!songList[MediaPlayerClass.getCurrentIndex()].title.isNullOrEmpty()) {
            binding?.songTitle?.text = songList[MediaPlayerClass.getCurrentIndex()].title
        }

        if (!songList[MediaPlayerClass.getCurrentIndex()].duration.isNullOrEmpty()) {
            binding?.totalTime?.text =
                convertToMMSS(songList[MediaPlayerClass.getCurrentIndex()].duration)
        }

    }

    private fun getDataFromIntent() {
        songList = intent.getSerializableExtra("list") as ArrayList<AudioModel>

    }

    fun convertToMMSS(duration: String): String? {
        val millis = duration.toLong()
        return java.lang.String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        )
    }
}