# SeekhoApp
Seekho Anime is a modern Android application that allows users to discover top-rated anime series, view detailed information, and watch trailers. The app is designed with offline capabilities, caching anime data locally using Room so users can access content even without an internet connection.

Key highlights:

Fetches data from the Jikan API (MyAnimeList) for top anime and anime details.

Displays trailers using ExoPlayer; falls back to WebView or poster images if video playback fails.

Caches anime data locally for offline access and syncs automatically when online.

Implements MVVM architecture with LiveData for reactive UI updates.

Handles errors gracefully (API failures, network changes, database issues).

Built with Kotlin, Retrofit, Glide, Room.

Features
1. Anime List Page

Shows popular or top-rated anime fetched from the API:

Title

Number of Episodes

Rating (MyAnimeList score)

Poster Image

API Endpoint: https://api.jikan.moe/v4/top/anime

2. Anime Detail Page

Displays detailed information for a selected anime:

Trailer video (or fallback to poster image)

Title and Synopsis

Genres

Main Cast

Number of Episodes

Rating

API Endpoint: https://api.jikan.moe/v4/anime/{anime_id}

3. Offline Mode

Data stored locally using Room Database.

Users can access anime details even when offline.

Data syncs automatically when the network is available.

4. Error Handling

Graceful handling for API errors, network issues, and database exceptions.

UI fallback ensures consistent experience regardless of connectivity.

5. Design & Architecture

MVVM architecture ensures maintainable and testable code.

Uses Retrofit for API calls, Glide for image loading, and LiveData for reactive UI.

Supports edge cases and UI constraints (e.g., profile images missing).
