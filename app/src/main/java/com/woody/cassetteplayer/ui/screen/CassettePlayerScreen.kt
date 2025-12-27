package com.woody.cassetteplayer.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.woody.cassetteplayer.data.model.PlaybackState
import com.woody.cassetteplayer.ui.components.AlbumListView
import com.woody.cassetteplayer.ui.components.CassetteControls
import com.woody.cassetteplayer.ui.components.CassetteViewPhotoWithReel
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

    val albums by viewModel.albums.collectAsState()
    val selectedAlbum by viewModel.selectedAlbum.collectAsState()
    val songs by viewModel.songs.collectAsState()
    val isRepeatMode by viewModel.isRepeatMode.collectAsState()

    // UI表示状態の管理
    var showControls by remember { mutableStateOf(true) }

    // 自動非表示タイマー
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(4000) // 4秒後に非表示
            showControls = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2C2C2C))
    ) {
        // Cassette tape with overlays (portrait mode)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // タップでコントロールを表示/非表示
                            showControls = !showControls
                        }
                    )
                }
        ) {
            // Cassette image
            CassetteViewPhotoWithReel(
                isPlaying = isPlaying,
                modifier = Modifier.fillMaxSize()
            )

            // Controls overlay (auto-hide)
            androidx.compose.animation.AnimatedVisibility(
                visible = showControls,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Song info overlay at top
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = currentSong?.title ?: "曲を選択してください",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        currentSong?.let { song ->
                            Text(
                                text = song.artist,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Controls overlay at bottom
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                            .padding(vertical = 16.dp)
                    ) {
                        // Control buttons
                        CassetteControls(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            isPlaying = isPlaying,
                            onPlayPause = {
                                showControls = true // ボタン操作時に表示を更新
                                if (isPlaying) {
                                    viewModel.pause()
                                } else {
                                    viewModel.play()
                                }
                            },
                            onStop = {
                                showControls = true
                                viewModel.stop()
                            },
                            onPrevious = {
                                showControls = true
                                viewModel.playPrevious()
                            },
                            onNext = {
                                showControls = true
                                viewModel.playNext()
                            },
                            onEject = {
                                showControls = true
                                viewModel.eject()
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Status and repeat mode in a row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            // Status text
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
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
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }

                            // Repeat mode toggle
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "リピート",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Switch(
                                    checked = isRepeatMode,
                                    onCheckedChange = {
                                        showControls = true
                                        viewModel.toggleRepeatMode()
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFF4CAF50),
                                        checkedTrackColor = Color(0xFF4CAF50).copy(alpha = 0.5f),
                                        uncheckedThumbColor = Color.White.copy(alpha = 0.5f),
                                        uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Album/Playlist Section
        if (selectedAlbum == null) {
            // Show album list
            AlbumListView(
                albums = albums,
                onAlbumClick = { album ->
                    viewModel.selectAlbum(album)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            )
        } else {
            // Show playlist for selected album
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            ) {
                // Back button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E))
                        .clickable { viewModel.backToAlbumList() }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "← アルバムリストに戻る",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

                // Playlist
                com.woody.cassetteplayer.ui.components.PlaylistView(
                    songs = songs,
                    currentSong = currentSong,
                    onSongClick = { song ->
                        viewModel.playSong(song)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CassettePlayerScreenPreview() {
    WoodyTheme {
        CassettePlayerScreen()
    }
}
