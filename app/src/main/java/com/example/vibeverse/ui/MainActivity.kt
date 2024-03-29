package com.example.vibeverse.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.example.vibeverse.R
import com.example.vibeverse.adapters.SwipeSongAdapter
import com.example.vibeverse.data.entities.Song
import com.example.vibeverse.databinding.ActivityMainBinding
import com.example.vibeverse.exoplayer.isPlaying
import com.example.vibeverse.exoplayer.toSong
import com.example.vibeverse.other.Status
import com.example.vibeverse.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter

    @Inject
    lateinit var glide: RequestManager

    private var curPlayingSong: Song? = null

    private var playbackState : PlaybackStateCompat?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        subscribeToObservers()

        binding.vpSong.adapter = swipeSongAdapter

        binding.vpSong.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if(playbackState?.isPlaying== true){
                    mainViewModel.playOrToggleSong(swipeSongAdapter.songs[position])
                }else{
                    curPlayingSong = swipeSongAdapter.songs[position]
                }
            }
        })

        binding.ivPlayPause.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it,true)
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)

        swipeSongAdapter.setOnClickListener {
            navHostFragment?.findNavController()?.navigate(
                R.id.globalActionToSongFragment
            )
        }



        navHostFragment?.findNavController()?.addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.songFragment -> hideBottomBar()
                R.id.homeFragment -> showBottomBar()
                else -> showBottomBar()
            }

        }

    }

    private fun hideBottomBar(){
        binding.vpSong.isVisible = false
        binding.ivPlayPause.isVisible = false
        binding.ivCurSongImage.isVisible = false
    }

    private fun showBottomBar(){
        binding.vpSong.isVisible = true
        binding.ivPlayPause.isVisible = true
        binding.ivCurSongImage.isVisible = true
    }

    private fun switchViewPagerToCurrentSong(song: Song){
        val newItemIndex = swipeSongAdapter.songs.indexOf(song)
        if(newItemIndex != -1){
            binding.vpSong.currentItem = newItemIndex
            curPlayingSong = song
        }
    }

    private fun subscribeToObservers(){
        mainViewModel.mediaItems.observe(this){
            it?.let {result ->
                when(result.status){
                    Status.SUCCESS ->{
                        result.data?.let {songs ->  
                            swipeSongAdapter.songs = songs
                            if (songs.isNotEmpty()){
                                glide.load((curPlayingSong?: songs[0]).imageUrl).into(binding.ivCurSongImage)
                            }
                            switchViewPagerToCurrentSong(curPlayingSong?: return@observe)
                        }
                    }
                    Status.ERROR -> Unit
                    Status.LOADING -> Unit
                }

            }
        }

        mainViewModel.curPlayingSong.observe(this){
            if(it == null) return@observe

            curPlayingSong = it.toSong()
            glide.load(curPlayingSong?.imageUrl).into(binding.ivCurSongImage)
            switchViewPagerToCurrentSong(curPlayingSong?: return@observe)
        }

        mainViewModel.playbackState.observe(this){
            playbackState = it
            binding.ivPlayPause.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_play else R.drawable.ic_pause
            )
        }

        mainViewModel.isConnected.observe(this){
            it?.getContentIfNotHandled()?.let {result->
                when(result.status){
                    Status.ERROR -> Snackbar.make(
                        binding.rootLayout,
                        result.message ?: "An unknown error occured",
                        Snackbar.LENGTH_LONG
                        ).show()
                    else -> Unit
                }
            }
        }

        mainViewModel.networkError.observe(this){
            it?.getContentIfNotHandled()?.let {result->
                when(result.status){
                    Status.ERROR -> Snackbar.make(
                        binding.rootLayout,
                        result.message ?: "An unknown error occured",
                        Snackbar.LENGTH_LONG
                    ).show()
                    else -> Unit
                }
            }
        }

    }
}