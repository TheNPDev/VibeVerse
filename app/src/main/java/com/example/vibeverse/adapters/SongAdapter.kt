package com.example.vibeverse.adapters


import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.RequestManager
import com.example.vibeverse.R
import com.google.android.material.textview.MaterialTextView
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val glide: RequestManager
) : BaseSongAdapter(R.layout.list_item) {

    override val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.itemView.apply {
            val tvPrimary = findViewById<MaterialTextView>(R.id.tvPrimary)
            val tvSecondary = findViewById<MaterialTextView>(R.id.tvSecondary)
            val ivItemImage = findViewById<ImageView>(R.id.ivItemImage)
            tvPrimary.text = song.title
            tvSecondary.text = song.subtitle
            glide.load(song.imageUrl).into(ivItemImage)

            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                }
            }
        }
    }
}