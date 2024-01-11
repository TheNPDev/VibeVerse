package com.example.vibeverse.ui.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.example.vibeverse.R
import com.example.vibeverse.data.entities.Song
import com.example.vibeverse.databinding.FragmentHomeBinding
import com.example.vibeverse.databinding.FragmentSongBinding
import com.example.vibeverse.exoplayer.isPlaying
import com.example.vibeverse.exoplayer.toSong
import com.example.vibeverse.other.Status
import com.example.vibeverse.ui.viewmodels.MainViewModel
import com.example.vibeverse.ui.viewmodels.SongViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment : Fragment(R.layout.fragment_song) {

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var glide: RequestManager

    private lateinit var mainViewModel: MainViewModel
    private val songViewModel :SongViewModel by viewModels()

    private var curPlayingSong : Song? = null

    private var playbackState : PlaybackStateCompat? = null

    private var shouldUpdateSeekbar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSongBinding.bind(view)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        subscribeToObservers()

        binding.ivPlayPauseDetail.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it,true)
            }
        }

        binding.ivSkipPrevious.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }

        binding.ivSkip.setOnClickListener {
            mainViewModel.skipToNextSong()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })
    }

    private fun updateTitleAndSongImage(song: Song) {
        val title = "${song.title} - ${song.subtitle}"
        binding.tvSongName.text = title
        glide.load(song.imageUrl).into(binding.ivSongImage)
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                when(result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { songs ->
                            if(curPlayingSong == null && songs.isNotEmpty()) {
                                curPlayingSong = songs[0]
                                updateTitleAndSongImage(songs[0])
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            if(it == null) return@observe
            curPlayingSong = it.toSong()
            updateTitleAndSongImage(curPlayingSong!!)
        }

        mainViewModel.playbackState.observe(viewLifecycleOwner){
            playbackState = it
            binding.ivPlayPauseDetail.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_play else R.drawable.ic_pause
            )
            binding.seekBar.progress = it?.position?.toInt() ?: 0
        }

        songViewModel.curPlayerPosition.observe(viewLifecycleOwner){
            if(shouldUpdateSeekbar){
                if (it != null) {
                    binding.seekBar.progress = it.toInt()
                    setCurPlayerTimeToTextView(it)
                }

            }
        }

        songViewModel.curSongDuration.observe(viewLifecycleOwner) { totalDuration ->
            // Check if the total duration is valid (not negative)
            if (totalDuration >= 0) {
                // Set the total duration as the max value of the seekBar
                binding.seekBar.max = totalDuration.toInt()

                // Format and display the total duration
                val minutes = TimeUnit.MILLISECONDS.toMinutes(totalDuration)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(totalDuration - TimeUnit.MINUTES.toMillis(minutes))
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                binding.tvSongDuration.text = formattedTime
            } else {
                // Handle the case when the total duration is not valid
                binding.seekBar.max = 0
                binding.tvSongDuration.text = "00:00"
            }
        }
    }

    private fun setCurPlayerTimeToTextView(ms: Long){
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms - TimeUnit.MINUTES.toMillis(minutes))

        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.tvCurTime.text = formattedTime.format(ms)
    }
}