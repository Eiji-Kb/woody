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
    private var onPlaylistEndCallback: (() -> Unit)? = null

    init {
        // リピートモードを設定（デフォルトはリピート）
        player.repeatMode = Player.REPEAT_MODE_ALL

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
                        // リピートOFFの場合、プレイリスト終了のコールバックを呼ぶ
                        if (player.repeatMode == Player.REPEAT_MODE_OFF) {
                            onPlaylistEndCallback?.invoke()
                        }
                        _playbackState.value = PlaybackState.Stopped
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

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                // 曲が変わったときに現在の曲を更新
                val currentMediaItemIndex = player.currentMediaItemIndex
                if (currentMediaItemIndex in playlist.indices) {
                    currentIndex = currentMediaItemIndex
                    _currentSong.value = playlist[currentIndex]
                }
            }
        })
    }

    fun setPlaylist(songs: List<Song>, startIndex: Int = 0) {
        playlist = songs
        currentIndex = startIndex

        if (songs.isEmpty()) return

        // プレイリスト全体をExoPlayerに設定
        val mediaItems = songs.map { song -> MediaItem.fromUri(song.uri) }
        player.setMediaItems(mediaItems, startIndex, 0)
        player.prepare()
        player.play()

        // 現在の曲を更新
        if (startIndex in songs.indices) {
            _currentSong.value = songs[startIndex]
        }
    }

    private fun playSongAtIndex(index: Int) {
        if (index !in playlist.indices) return

        currentIndex = index
        _currentSong.value = playlist[index]

        // ExoPlayerの曲インデックスに移動
        player.seekTo(index, 0)
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
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
        } else {
            player.seekTo(0, 0)
        }
    }

    fun playPrevious() {
        if (playlist.isEmpty()) return
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
        } else {
            player.seekTo(playlist.size - 1, 0)
        }
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

    fun setRepeatMode(isRepeat: Boolean) {
        player.repeatMode = if (isRepeat) {
            Player.REPEAT_MODE_ALL
        } else {
            Player.REPEAT_MODE_OFF
        }
    }

    fun setOnPlaylistEndCallback(callback: () -> Unit) {
        onPlaylistEndCallback = callback
    }

    fun release() {
        player.release()
    }
}
