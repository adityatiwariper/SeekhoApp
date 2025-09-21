# SeekhoApp
Seekho Anime is a modern Android application that allows users to discover top-rated anime series, view detailed information, and watch trailers. The app is designed with offline capabilities, caching anime data locally using Room so users can access content even without an internet connection.

Key highlights:

Fetches data from the Jikan API (MyAnimeList) for top anime and anime details.

Displays trailers using ExoPlayer; falls back to WebView or poster images if video playback fails.

Caches anime data locally for offline access and syncs automatically when online.

Implements MVVM architecture with LiveData for reactive UI updates.

Handles errors gracefully (API failures, network changes, database issues).

Built with Kotlin, Retrofit, Glide, Room .
