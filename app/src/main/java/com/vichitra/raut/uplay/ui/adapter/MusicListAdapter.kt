package com.vichitra.raut.uplay.ui.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vichitra.raut.uplay.R
import com.vichitra.raut.uplay.data.AudioModel
import com.vichitra.raut.uplay.databinding.MusicItemBinding
import com.vichitra.raut.uplay.ui.MediaPlayerActivity
import com.vichitra.raut.uplay.ui.MediaPlayerClass

class MusicListAdapter(var musicList: ArrayList<AudioModel>, var context: Context): RecyclerView.Adapter<MusicListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songData = musicList[position]

        holder.text.text = songData.title



        holder.itemView.setOnClickListener {
            MediaPlayerClass.getInstance().reset()
            MediaPlayerClass.setCurrentIndex(position)
            var intent = Intent(context, MediaPlayerActivity::class.java)
            intent.putExtra("list",musicList)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var text : TextView = itemView.findViewById(R.id.musicName)
    }
}
