package com.woody.cassetteplayer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.woody.cassetteplayer.data.model.PlaybackState
import com.woody.cassetteplayer.ui.components.CassetteControls
import com.woody.cassetteplayer.ui.components.CassetteViewPhoto
import com.woody.cassetteplayer.ui.theme.WoodyTheme
import com.woody.cassetteplayer.ui.viewmodel.CassettePlayerViewModel

@Composable
fun CassettePlayerScreen(
    viewModel: CassettePlayerViewModel = hiltViewModel()
) {
    val playbackState by viewModel.playbackState.collectAsState()
    val currentSong by viewModel.currentSong.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val isPlaying = playbackState is PlaybackState.Playing

    val songs by viewModel.songs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
    ) {
        // Player Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .background(Color(0xFF2C2C2C))
        ) {
            Text(
                text = "Woody Cassette Player",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            // Current song info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = currentSong?.title ?: "曲を選択してください",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentSong?.artist ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cassette tape (rotated 90 degrees for portrait mode)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CassetteViewPhoto(
                    isPlaying = isPlaying,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.6f)
                        .rotate(90f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            CassetteControls(
                modifier = Modifier.padding(horizontal = 16.dp),
                isPlaying = isPlaying,
                onPlayPause = {
                    if (isPlaying) {
                        viewModel.pause()
                    } else {
                        viewModel.play()
                    }
                },
                onStop = { viewModel.stop() },
                onPrevious = { viewModel.playPrevious() },
                onNext = { viewModel.playNext() },
                onEject = { viewModel.eject() }
            )

            // Status text
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = when (playbackState) {
                        is PlaybackState.Playing -> "再生中"
                        is PlaybackState.Paused -> "一時停止"
                        is PlaybackState.Stopped -> "停止"
                        is PlaybackState.Idle -> "準備完了"
                        is PlaybackState.Error -> "エラー"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Playlist Section
        com.woody.cassetteplayer.ui.components.PlaylistView(
            songs = songs,
            currentSong = currentSong,
            onSongClick = { song ->
                viewModel.playSong(song)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CassettePlayerScreenPreview() {
    WoodyTheme {
        CassettePlayerScreen()
    }
}
