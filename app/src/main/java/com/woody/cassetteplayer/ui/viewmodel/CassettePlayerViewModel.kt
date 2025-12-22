package com.woody.cassetteplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val playbackState: StateFlow<PlaybackState> = musicPlayer.playbackState
    val currentSong: StateFlow<Song?> = musicPlayer.currentSong

    init {
        loadSongs()
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

    override fun onCleared() {
        super.onCleared()
        musicPlayer.release()
        soundEffectManager.release()
    }
}
