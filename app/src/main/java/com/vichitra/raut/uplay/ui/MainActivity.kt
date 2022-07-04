package com.vichitra.raut.uplay.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.vichitra.raut.uplay.data.AudioModel
import com.vichitra.raut.uplay.databinding.ActivityMainBinding
import com.vichitra.raut.uplay.ui.adapter.MusicListAdapter
import java.io.File

class MainActivity : AppCompatActivity() {

    private var songList: ArrayList<AudioModel> = arrayListOf()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPermission()

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
            ), selection, null, null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                var duration = cursor.getString(2)
                if (duration == null) {
                    duration = ""
                }
                var songData = AudioModel(cursor.getString(1), cursor.getString(0), duration)
                if (File(songData.path).exists())
                    songList.add(songData)
            }
        }

        if (songList.size == 0) {
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            binding.recyclerView.adapter = MusicListAdapter(songList, this)
        }
    }

    private fun userPermission() {
        if (!checkPermission()) {
            requestPermission()
            return
        }
    }


    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this,
                "Read permission is required,please allow from settings",
                Toast.LENGTH_SHORT
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }
}