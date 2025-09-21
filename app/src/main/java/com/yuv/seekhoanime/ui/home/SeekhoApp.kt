package com.yuv.seekhoanime.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuv.seekhoanime.data.remote.local.AnimeDatabase
import com.yuv.seekhoanime.data.remote.repository.AnimeRepository
import com.yuv.seekhoanime.databinding.ActivityMainBinding
import com.yuv.seekhoanime.ui.home.adapter.AnimeAdapter
import com.yuv.seekhoanime.ui.home.viewmodel.AnimeViewModel
import com.yuv.seekhoanime.ui.home.viewmodel.AnimeViewModelFactory

class SeekhoApp : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AnimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerViewAnime.layoutManager = LinearLayoutManager(this)
        // Initialize Repository & ViewModelFactory
        val dao = AnimeDatabase.getInstance(this).animeDao()
        val repository = AnimeRepository(dao)
        val factory = AnimeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AnimeViewModel::class.java]
        viewModel.refreshAnime()
        viewModel.animeList.observe(this) { list ->
            binding.progressBar.visibility = View.GONE
            binding.recyclerViewAnime.adapter = AnimeAdapter(list) { anime ->
                val intent = Intent(this, AnimeDetailActivity::class.java)
                intent.putExtra("anime_id", anime.malId)
                startActivity(intent)
            }
        }

    }
}