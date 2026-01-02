package com.woody.cassetteplayer.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woody.cassetteplayer.R
import com.woody.cassetteplayer.ui.theme.WoodyTheme

@Composable
fun CassetteViewPhotoWithReel(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    // アニメーション: リールの回転
    val infiniteTransition = rememberInfiniteTransition(label = "reel_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // レイヤー1（最下層）: 濃いめのグレー背景
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF909090))  // 濃いめのグレー0xFF707070
        )

        // レイヤー2: 上のリール（回転）
        Image(
            painter = painterResource(id = R.drawable.cassette_reel),
            contentDescription = "Top Reel",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 19.dp, y = (-101).dp)  // 上に配置（調整が必要）
                .size(120.dp)  // リールのサイズ（調整が必要）
                .rotate(if (isPlaying) rotation else 0f),
            contentScale = ContentScale.Fit
        )

        // レイヤー3: 下のリール（回転）
        Image(
            painter = painterResource(id = R.drawable.cassette_reel),
            contentDescription = "Bottom Reel",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 19.dp, y = 101.dp)  // 下に配置（調整が必要）
                .size(120.dp)  // リールのサイズ（調整が必要）
                .rotate(if (isPlaying) rotation else 0f),
            contentScale = ContentScale.Fit
        )

        // レイヤー4（最上層）: カセット本体の画像（わずかに半透過で自然に）
        Image(
            painter = painterResource(id = R.drawable.cassette_body),
            contentDescription = "Cassette Body",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.95f),  // わずかに半透過にしてリールが透けて見えるように
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CassetteViewPhotoWithReelPreview() {
    WoodyTheme {
        CassetteViewPhotoWithReel(
            isPlaying = false,
            modifier = Modifier.size(400.dp, 600.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CassetteViewPhotoWithReelPlayingPreview() {
    WoodyTheme {
        CassetteViewPhotoWithReel(
            isPlaying = true,
            modifier = Modifier.size(400.dp, 600.dp)
        )
    }
}
