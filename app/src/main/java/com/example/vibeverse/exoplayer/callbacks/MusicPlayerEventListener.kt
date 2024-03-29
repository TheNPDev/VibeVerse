package com.example.vibeverse.exoplayer.callbacks

import android.widget.Toast
import com.example.vibeverse.exoplayer.MusicService
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.STATE_READY

class MusicPlayerEventListener(
    private val musicService: MusicService
): Player.Listener {
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if(playbackState == STATE_READY && !playWhenReady){
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService,"An unknown error occured" ,Toast.LENGTH_LONG).show()
    }
}