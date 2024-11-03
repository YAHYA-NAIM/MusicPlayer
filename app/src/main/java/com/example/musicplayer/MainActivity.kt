package com.example.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var trackNameTextView: TextView
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrevious: ImageButton
    private var isPlaying = false
    private var currentTrackIndex = 0

    private val musicList = listOf(
        Music("Hanya Lwa9t", "Abduh feat Ouenza", Uri.parse("android.resource://com.example.musicplayer/" + R.raw.hanyalwaqt)),
        Music("Gatsby", "Akra", Uri.parse("android.resource://com.example.musicplayer/" + R.raw.akragatsby)),
        Music("Too Many Nights", "Metro Boomin", Uri.parse("android.resource://com.example.musicplayer/" + R.raw.toomanynights))
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        trackNameTextView = findViewById(R.id.trackName)
        val editText: EditText = findViewById(R.id.editTextText)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)

        musicAdapter = MusicAdapter(musicList) { uri, title ->
            playMusic(uri, title)
        }
        recyclerView.adapter = musicAdapter

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().toLowerCase()
                val filteredList = musicList.filter {
                    it.title.toLowerCase().contains(query) || it.artist.toLowerCase().contains(query)
                }
                musicAdapter.updateList(filteredList)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                pauseMusic()
            } else {
                mediaPlayer?.start()
                updatePlayPauseIcon(true)
            }
        }

        btnNext.setOnClickListener { playNextTrack() }
        btnPrevious.setOnClickListener { playPreviousTrack() }
    }

    private fun playMusic(uri: Uri, title: String) {
        stopMusic() // Release the current player
        mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer?.start()
        trackNameTextView.text = title
        updatePlayPauseIcon(true)
        isPlaying = true

        mediaPlayer?.setOnCompletionListener {
            updatePlayPauseIcon(false)
        }
    }

    private fun playNextTrack() {
        if (currentTrackIndex < musicList.size - 1) {
            currentTrackIndex++
        } else {
            currentTrackIndex = 0
        }
        val nextTrack = musicList[currentTrackIndex]
        playMusic(nextTrack.audioUri, nextTrack.title)
    }

    private fun playPreviousTrack() {
        if (currentTrackIndex > 0) {
            currentTrackIndex--
        } else {
            currentTrackIndex = musicList.size - 1
        }
        val previousTrack = musicList[currentTrackIndex]
        playMusic(previousTrack.audioUri, previousTrack.title)
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        updatePlayPauseIcon(false)
        isPlaying = false
    }

    private fun stopMusic() {
        mediaPlayer?.release()
        mediaPlayer = null
        updatePlayPauseIcon(false)
        isPlaying = false
    }

    private fun updatePlayPauseIcon(isPlayingNow: Boolean) {
        btnPlayPause.setImageResource(
            if (isPlayingNow) R.drawable.play else R.drawable.stop
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
