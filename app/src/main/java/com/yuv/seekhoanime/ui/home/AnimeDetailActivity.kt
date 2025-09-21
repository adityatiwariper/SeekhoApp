package com.yuv.seekhoanime.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.yuv.seekhoanime.ui.home.viewmodel.AnimeViewModel


import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.yuv.seekhoanime.R
import com.yuv.seekhoanime.data.remote.local.AnimeDatabase
import com.yuv.seekhoanime.data.remote.local.entity.AnimeEntity
import com.yuv.seekhoanime.data.remote.repository.AnimeRepository
import com.yuv.seekhoanime.databinding.ActivityDetailBinding
import com.yuv.seekhoanime.ui.home.viewmodel.AnimeViewModelFactory

class AnimeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var player: ExoPlayer? = null
    private val viewModel: AnimeViewModel by viewModels {
        val dao = AnimeDatabase.getInstance(applicationContext).animeDao()
        val repository = AnimeRepository(dao)
        AnimeViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get anime ID from intent
        val animeId = intent.getIntExtra("anime_id", 0)
        if (animeId != -1 && isNetworkAvailable()) {
            viewModel.loadAnimeDetails(animeId)
        }




        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe errors
        viewModel.error.observe(this) { errorMsg ->
            if (errorMsg != null) {
                binding.textSynopsis.text = errorMsg
            }
        }


        // Observe online details
        viewModel.animeDetail.observe(this) { detail ->
            if (detail != null && isNetworkAvailable()) {
                binding.progressBar.visibility = View.GONE
                showContent(detail)
            }
        }



        viewModel.animeList.observe(this) { list ->
            if (!isNetworkAvailable()) {
                binding.progressBar.visibility = View.GONE
                val animeOffline = list.find { it.malId == animeId }
                animeOffline?.let { loadOfflineContent(it) }
            }
        }


    }

    private fun showContent(detail: com.yuv.seekhoanime.data.remote.models.AnimeDetailResponse) {
        // Always show text
        binding.textTitleDetail.text = detail.data.title
        binding.textSynopsis.text = detail.data.synopsis ?: "No synopsis available"
        binding.textEpisodesDetail.text = "Episodes: ${detail.data.episodes ?: "?"}"
        binding.textRatingDetail.text = "Rating: ${detail.data.score ?: "N/A"}"
        binding.textGenres.text = detail.data.genres.joinToString { g -> g.name }

        // Try ExoPlayer first
        if (!detail.data.trailer?.url.isNullOrEmpty()) {
            try {
                player = ExoPlayer.Builder(this).build()
                binding.playerView.player = player
                player?.setMediaItem(MediaItem.fromUri(Uri.parse(detail.data.trailer!!.url)))
                player?.prepare()
                player?.play()

                player?.addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        // Fallback to WebView
                        loadWebViewOrPoster(detail)
                    }
                })

                binding.playerView.visibility = View.VISIBLE
                binding.webView.visibility = View.GONE
                binding.imagePosterDetail.visibility = View.GONE

            } catch (e: Exception) {
                loadWebViewOrPoster(detail)
            }
        } else {
            loadWebViewOrPoster(detail)
        }
    }

    private fun loadWebViewOrPoster(detail: com.yuv.seekhoanime.data.remote.models.AnimeDetailResponse) {
        if (!detail.data.trailer?.embed_url.isNullOrEmpty() && isNetworkAvailable()) {
            try {
                binding.webView.settings.javaScriptEnabled = true
                binding.webView.webViewClient = object : WebViewClient() {
                    override fun onReceivedError(
                        view: android.webkit.WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        // WebView failed → show poster
                        loadPoster(detail)
                    }
                }
                binding.webView.loadUrl(detail.data.trailer!!.embed_url!!)
                binding.webView.visibility = View.VISIBLE
                binding.playerView.visibility = View.GONE
                binding.imagePosterDetail.visibility = View.GONE
            } catch (e: Exception) {
                loadPoster(detail)
            }
        } else {
            loadPoster(detail)
        }
    }

    private fun loadPoster(detail: com.yuv.seekhoanime.data.remote.models.AnimeDetailResponse) {
        if (!isDestroyed) {
            Glide.with(this)
                .load(detail.data.images.jpg.image_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.game)
                .into(binding.imagePosterDetail)
        }
        binding.imagePosterDetail.visibility = View.VISIBLE
        binding.playerView.visibility = View.GONE
        binding.webView.visibility = View.GONE
    }

    private fun loadOfflineContent(anime: AnimeEntity) {
        binding.textTitleDetail.text = anime.title
        binding.textSynopsis.text = anime.synopsis ?: "No synopsis available"
        binding.textEpisodesDetail.text = "Episodes: ${anime.episodes ?: "?"}"
        binding.textRatingDetail.text = "Rating: ${anime.score ?: "N/A"}"
        binding.textGenres.text = anime.genres

        if (!isDestroyed) {
            Glide.with(this)
                .load(anime.posterUrl ?: R.drawable.game)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.game)
                .into(binding.imagePosterDetail)
        }

        binding.imagePosterDetail.visibility = View.VISIBLE
        binding.playerView.visibility = View.GONE
        binding.webView.visibility = View.GONE
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}