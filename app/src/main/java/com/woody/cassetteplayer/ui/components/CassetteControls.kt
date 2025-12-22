package com.woody.cassetteplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.woody.cassetteplayer.ui.theme.WoodyTheme

@Composable
fun CassetteControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onStop: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onEject: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous Button
        CassetteButton(
            icon = Icons.Default.SkipPrevious,
            onClick = onPrevious,
            contentDescription = "Previous"
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Stop Button
        CassetteButton(
            icon = Icons.Default.Stop,
            onClick = onStop,
            contentDescription = "Stop",
            backgroundColor = Color(0xFFD32F2F)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Play/Pause Button
        CassetteButton(
            icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            onClick = onPlayPause,
            contentDescription = if (isPlaying) "Pause" else "Play",
            backgroundColor = Color(0xFF388E3C),
            size = 56.dp
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Fast Forward (Next) Button
        CassetteButton(
            icon = Icons.Default.SkipNext,
            onClick = onNext,
            contentDescription = "Next"
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Eject Button
        CassetteButton(
            icon = Icons.Default.Eject,
            onClick = onEject,
            contentDescription = "Eject",
            backgroundColor = Color(0xFF757575)
        )
    }
}

@Composable
fun CassetteButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF424242),
    size: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(2.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CassetteControlsPreview() {
    WoodyTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2C2C2C))
                .padding(16.dp)
        ) {
            CassetteControls(
                isPlaying = false,
                onPlayPause = {},
                onStop = {},
                onPrevious = {},
                onNext = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CassetteControlsPlayingPreview() {
    WoodyTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2C2C2C))
                .padding(16.dp)
        ) {
            CassetteControls(
                isPlaying = true,
                onPlayPause = {},
                onStop = {},
                onPrevious = {},
                onNext = {}
            )
        }
    }
}
