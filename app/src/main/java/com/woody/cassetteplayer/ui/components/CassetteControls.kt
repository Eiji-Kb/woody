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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous Button
        CassetteButton(
            icon = Icons.Default.SkipPrevious,
            onClick = onPrevious,
            contentDescription = "Previous",
            size = 52.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Stop Button
        CassetteButton(
            icon = Icons.Default.Stop,
            onClick = onStop,
            contentDescription = "Stop",
            backgroundColor = Color(0xFFD32F2F),
            size = 52.dp
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Play/Pause Button (larger)
        CassetteButton(
            icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            onClick = onPlayPause,
            contentDescription = if (isPlaying) "Pause" else "Play",
            backgroundColor = Color(0xFF388E3C),
            size = 64.dp
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Fast Forward (Next) Button
        CassetteButton(
            icon = Icons.Default.SkipNext,
            onClick = onNext,
            contentDescription = "Next",
            size = 52.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Eject Button
        CassetteButton(
            icon = Icons.Default.Eject,
            onClick = onEject,
            contentDescription = "Eject",
            backgroundColor = Color(0xFF757575),
            size = 52.dp
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
    val gradientColors = when (backgroundColor) {
        Color(0xFFD32F2F) -> listOf(Color(0xFFE53935), Color(0xFFC62828)) // Stop button - red
        Color(0xFF388E3C) -> listOf(Color(0xFF66BB6A), Color(0xFF2E7D32)) // Play button - green
        Color(0xFF757575) -> listOf(Color(0xFF9E9E9E), Color(0xFF616161)) // Eject - gray
        else -> listOf(Color(0xFF616161), Color(0xFF424242)) // Default - dark gray
    }

    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                clip = false
            )
            .clip(CircleShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            )
            .clickable(onClick = onClick)
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(size * 0.55f)
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
