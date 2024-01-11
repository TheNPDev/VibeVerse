package com.example.vibeverse.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import com.example.vibeverse.R
import com.google.android.material.textview.MaterialTextView

class SwipeSongAdapter : BaseSongAdapter(R.layout.swipe_item) {


    override val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
            val tvSwipe = findViewById<MaterialTextView>(R.id.tvSwipe)
            val text = "${song.title} - ${song.subtitle}"
            tvSwipe.text = text

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }

}