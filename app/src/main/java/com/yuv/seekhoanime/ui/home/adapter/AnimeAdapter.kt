package com.yuv.seekhoanime.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yuv.seekhoanime.data.remote.local.entity.AnimeEntity
import com.yuv.seekhoanime.databinding.ItemAnimeBinding

class AnimeAdapter(
    private val animeList: List<AnimeEntity>,
    private val onClick: (AnimeEntity) -> Unit
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    inner class AnimeViewHolder(val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.binding.textTitle.text = anime.title
        holder.binding.textEpisodes.text = "Episodes: ${anime.episodes ?: "?"}"
        holder.binding.textRating.text = "Rating: ${anime.score ?: "N/A"}"
        // Use posterUrl from AnimeEntity
        Glide.with(holder.itemView)
            .load(anime.posterUrl)
            .into(holder.binding.imagePoster)

        holder.itemView.setOnClickListener { onClick(anime) }
    }

    override fun getItemCount() = animeList.size
}