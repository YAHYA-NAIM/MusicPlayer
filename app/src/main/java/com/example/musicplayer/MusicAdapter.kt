package com.example.musicplayer

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class Music(val title: String, val artist: String, val audioUri: Uri, var isFavorite: Boolean = false)

class MusicAdapter(
    private var musicList: List<Music>,
    private val onPlayClick: (Uri, String) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songName: TextView = view.findViewById(R.id.songName)
        val artistName: TextView = view.findViewById(R.id.artistName)
        val startMusic: ImageView = view.findViewById(R.id.songImage)
        val heartIcon: ImageView = view.findViewById(R.id.heartIcon)

        fun bind(music: Music) {
            songName.text = music.title
            artistName.text = music.artist

            heartIcon.setImageResource(if (music.isFavorite) R.drawable.heart else R.drawable.heart1)

            startMusic.setOnClickListener { onPlayClick(music.audioUri, music.title) }

            heartIcon.setOnClickListener {
                music.isFavorite = !music.isFavorite
                heartIcon.setImageResource(if (music.isFavorite) R.drawable.heart else R.drawable.heart1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(musicList[position])
    }

    override fun getItemCount() = musicList.size

    fun updateList(newList: List<Music>) {
        musicList = newList
        notifyDataSetChanged()
    }
}
