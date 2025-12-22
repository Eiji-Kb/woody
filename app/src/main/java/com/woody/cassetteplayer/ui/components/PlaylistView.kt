package com.woody.cassetteplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            .background(Color(0xFF1E1E1E))
    ) {
        Text(
            text = "プレイリスト (${songs.size}曲)",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(songs) { song ->
                SongItem(
                    song = song,
                    isPlaying = song.id == currentSong?.id,
                    onClick = { onSongClick(song) }
                )
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
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isPlaying) Color(0xFF388E3C).copy(alpha = 0.3f)
                else Color(0xFF2C2C2C)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            tint = if (isPlaying) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isPlaying) Color(0xFF4CAF50) else Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            text = song.getFormattedDuration(),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.5f)
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
