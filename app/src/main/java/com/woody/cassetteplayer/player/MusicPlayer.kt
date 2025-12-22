package com.woody.cassetteplayer.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.woody.cassetteplayer.data.model.PlaybackState
import com.woody.cassetteplayer.data.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayer @Inject constructor(
    private val player: ExoPlayer
) {
    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private var playlist: List<Song> = emptyList()
    private var currentIndex: Int = -1

    init {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_IDLE -> _playbackState.value = PlaybackState.Idle
                    Player.STATE_BUFFERING -> {}
                    Player.STATE_READY -> {
                        if (player.playWhenReady) {
                            _playbackState.value = PlaybackState.Playing
                        } else {
                            _playbackState.value = PlaybackState.Paused
                        }
                    }
                    Player.STATE_ENDED -> {
                        playNext()
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    _playbackState.value = PlaybackState.Playing
                } else {
                    if (_playbackState.value is PlaybackState.Playing) {
                        _playbackState.value = PlaybackState.Paused
                    }
                }
            }
        })
    }

    fun setPlaylist(songs: List<Song>, startIndex: Int = 0) {
        playlist = songs
        currentIndex = startIndex
        if (songs.isNotEmpty() && startIndex in songs.indices) {
            playSongAtIndex(startIndex)
        }
    }

    private fun playSongAtIndex(index: Int) {
        if (index !in playlist.indices) return

        currentIndex = index
        val song = playlist[index]
        _currentSong.value = song

        val mediaItem = MediaItem.fromUri(song.uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun play() {
        if (_currentSong.value == null && playlist.isNotEmpty()) {
            playSongAtIndex(0)
        } else {
            player.play()
        }
    }

    fun pause() {
        player.pause()
    }

    fun stop() {
        player.stop()
        _playbackState.value = PlaybackState.Stopped
    }

    fun playNext() {
        if (playlist.isEmpty()) return
        val nextIndex = (currentIndex + 1) % playlist.size
        playSongAtIndex(nextIndex)
    }

    fun playPrevious() {
        if (playlist.isEmpty()) return
        val previousIndex = if (currentIndex - 1 < 0) {
            playlist.size - 1
        } else {
            currentIndex - 1
        }
        playSongAtIndex(previousIndex)
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun getCurrentPosition(): Long {
        return player.currentPosition
    }

    fun getDuration(): Long {
        return player.duration
    }

    fun release() {
        player.release()
    }
}
