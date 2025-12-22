package com.woody.cassetteplayer.data.model

sealed class PlaybackState {
    object Idle : PlaybackState()
    object Playing : PlaybackState()
    object Paused : PlaybackState()
    object Stopped : PlaybackState()
    data class Error(val message: String) : PlaybackState()
}
