package com.woody.cassetteplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woody.cassetteplayer.data.model.Song
import com.woody.cassetteplayer.ui.theme.WoodyTheme

@Composable
fun PlaylistView(
    songs: List<Song>,
    currentSong: Song?,
    onSongClick: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()  // 下まで白で埋める
            .background(Color.White)  // 白い紙のラベル
    ) {
        Text(
            text = "プレイリスト (${songs.size}曲)",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
        ) {
            itemsIndexed(songs) { index, song ->
                SongItem(
                    song = song,
                    isPlaying = song.id == currentSong?.id,
                    onClick = { onSongClick(song) }
                )

                // 曲と曲の間に線を表示（最後の曲以外）
                if (index < songs.size - 1) {
                    Divider(
                        modifier = Modifier.padding(vertical = 2.dp),
                        thickness = 1.dp,
                        color = Color.Black.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun SongItem(
    song: Song,
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (isPlaying) Color(0xFFFFF9C4)  // 薄い黄色（再生中をマーカーで塗った感じ）
                else Color.White  // 白い紙
            )
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 曲名（左側）
        Text(
            text = song.title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        // 時間（右側）
        Text(
            text = song.getFormattedDuration(),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.6f),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistViewPreview() {
    WoodyTheme {
        val sampleSongs = listOf(
            Song(
                id = 1,
                title = "Sample Song 1",
                artist = "Artist 1",
                album = "Album 1",
                duration = 180000,
                uri = android.net.Uri.EMPTY
            ),
            Song(
                id = 2,
                title = "Sample Song 2",
                artist = "Artist 2",
                album = "Album 2",
                duration = 240000,
                uri = android.net.Uri.EMPTY
            ),
            Song(
                id = 3,
                title = "Sample Song 3",
                artist = "Artist 3",
                album = "Album 3",
                duration = 200000,
                uri = android.net.Uri.EMPTY
            )
        )

        PlaylistView(
            songs = sampleSongs,
            currentSong = sampleSongs[0],
            onSongClick = {}
        )
    }
}
