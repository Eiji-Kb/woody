package com.woody.cassetteplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woody.cassetteplayer.data.model.Album
import com.woody.cassetteplayer.ui.theme.WoodyTheme

@Composable
fun AlbumListView(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2C))  // 暗めのグレー背景
    ) {
        Text(
            text = "アルバム (${albums.size})",
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,  // 太字ゴシック体
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(albums) { album ->
                AlbumItem(
                    album = album,
                    onClick = { onAlbumClick(album) }
                )
            }
        }
    }
}

@Composable
fun AlbumItem(
    album: Album,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)  // 白色
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
            )
            .drawWithContent {
                drawContent()
                // 透明プラスチックのような光沢効果（強調）
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.6f),  // より強い白いハイライト
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.15f)  // より強い影
                        ),
                        startY = 0f,
                        endY = size.height
                    )
                )
            }
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Album,
            contentDescription = null,
            tint = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            // アルバム名
            Text(
                text = album.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            // アーティスト名（右寄せ）
            Text(
                text = album.artist,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black.copy(alpha = 0.7f),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End  // 右寄せ
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlbumListViewPreview() {
    WoodyTheme {
        val sampleAlbums = listOf(
            Album(
                id = 1,
                name = "Sample Album 1",
                artist = "Artist 1",
                songCount = 12,
                albumArtUri = null
            ),
            Album(
                id = 2,
                name = "Sample Album 2",
                artist = "Artist 2",
                songCount = 8,
                albumArtUri = null
            ),
            Album(
                id = 3,
                name = "Sample Album 3",
                artist = "Artist 3",
                songCount = 15,
                albumArtUri = null
            )
        )

        AlbumListView(
            albums = sampleAlbums,
            onAlbumClick = {}
        )
    }
}
