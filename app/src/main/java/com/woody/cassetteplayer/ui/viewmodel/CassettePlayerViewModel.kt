package com.woody.cassetteplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woody.cassetteplayer.data.model.Album
import com.woody.cassetteplayer.data.model.PlaybackState
import com.woody.cassetteplayer.data.model.Song
import com.woody.cassetteplayer.data.repository.MusicRepository
import com.woody.cassetteplayer.player.MusicPlayer
import com.woody.cassetteplayer.sound.SoundEffect
import com.woody.cassetteplayer.sound.SoundEffectManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CassettePlayerViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val musicPlayer: MusicPlayer,
    private val soundEffectManager: SoundEffectManager
) : ViewModel() {

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums.asStateFlow()

    private val _selectedAlbum = MutableStateFlow<Album?>(null)
    val selectedAlbum: StateFlow<Album?> = _selectedAlbum.asStateFlow()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRepeatMode = MutableStateFlow(true)
    val isRepeatMode: StateFlow<Boolean> = _isRepeatMode.asStateFlow()

    val playbackState: StateFlow<PlaybackState> = musicPlayer.playbackState
    val currentSong: StateFlow<Song?> = musicPlayer.currentSong

    init {
        loadAlbums()

        // プレイリスト終了時のコールバックを設定
        musicPlayer.setOnPlaylistEndCallback {
            // 停止音を鳴らして停止
            soundEffectManager.play(SoundEffect.STOP)
            musicPlayer.stop()
        }
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val albumsList = musicRepository.getAllAlbums()
                _albums.value = albumsList
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAlbum(album: Album) {
        soundEffectManager.play(SoundEffect.BUTTON_CLICK)
        _selectedAlbum.value = album
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val songsList = musicRepository.getSongsByAlbum(album.id)
                _songs.value = songsList
                if (songsList.isNotEmpty()) {
                    musicPlayer.setPlaylist(songsList, 0)
                }
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun backToAlbumList() {
        soundEffectManager.play(SoundEffect.BUTTON_CLICK)
        _selectedAlbum.value = null
        _songs.value = emptyList()
        musicPlayer.stop()
    }

    fun loadSongs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val songsList = musicRepository.getAllSongs()
                _songs.value = songsList
                if (songsList.isNotEmpty()) {
                    musicPlayer.setPlaylist(songsList, 0)
                }
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun play() {
        soundEffectManager.play(SoundEffect.PLAY_START)
        musicPlayer.play()
    }

    fun pause() {
        soundEffectManager.play(SoundEffect.PAUSE)
        musicPlayer.pause()
    }

    fun stop() {
        soundEffectManager.play(SoundEffect.STOP)
        musicPlayer.stop()
    }

    fun playNext() {
        soundEffectManager.play(SoundEffect.MECHANICAL)
        musicPlayer.playNext()
    }

    fun playPrevious() {
        soundEffectManager.play(SoundEffect.MECHANICAL)
        musicPlayer.playPrevious()
    }

    fun playSong(song: Song) {
        soundEffectManager.play(SoundEffect.BUTTON_CLICK)
        val index = _songs.value.indexOf(song)
        if (index != -1) {
            musicPlayer.setPlaylist(_songs.value, index)
        }
    }

    fun seekTo(position: Long) {
        musicPlayer.seekTo(position)
    }

    fun eject() {
        soundEffectManager.play(SoundEffect.EJECT)
        musicPlayer.stop()
    }

    fun toggleRepeatMode() {
        soundEffectManager.play(SoundEffect.BUTTON_CLICK)
        _isRepeatMode.value = !_isRepeatMode.value
        musicPlayer.setRepeatMode(_isRepeatMode.value)
    }

    override fun onCleared() {
        super.onCleared()
        musicPlayer.release()
        soundEffectManager.release()
    }
}
