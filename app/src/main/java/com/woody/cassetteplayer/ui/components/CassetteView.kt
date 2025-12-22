package com.woody.cassetteplayer.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woody.cassetteplayer.ui.theme.WoodyTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CassetteView(
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
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E3A5F))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // カセットテープの本体
            drawRect(
                color = Color(0xFF1E3A5F),
                topLeft = Offset.Zero,
                size = Size(width, height)
            )

            // ラベル部分（上部）
            drawRect(
                color = Color(0xFFE8E8E8),
                topLeft = Offset(width * 0.1f, height * 0.08f),
                size = Size(width * 0.8f, height * 0.15f)
            )

            // 透明な窓部分
            drawRect(
                color = Color(0x88555555),
                topLeft = Offset(width * 0.1f, height * 0.28f),
                size = Size(width * 0.8f, height * 0.45f)
            )

            // 左リール
            val leftReelCenter = Offset(width * 0.3f, height * 0.5f)
            val reelRadius = width * 0.12f

            // 右リール
            val rightReelCenter = Offset(width * 0.7f, height * 0.5f)

            // リールの描画（回転アニメーション付き）
            listOf(leftReelCenter, rightReelCenter).forEach { center ->
                // リール外側の白い円
                drawCircle(
                    color = Color(0xFFF0F0F0),
                    radius = reelRadius,
                    center = center
                )

                // リール中央の穴
                drawCircle(
                    color = Color(0xFF333333),
                    radius = reelRadius * 0.3f,
                    center = center
                )

                // リールの歯車模様（回転）
                if (isPlaying) {
                    rotate(rotation, pivot = center) {
                        for (i in 0 until 12) {
                            val angle = (i * 30).toFloat()
                            val rad = Math.toRadians(angle.toDouble())
                            val startRadius = reelRadius * 0.35f
                            val endRadius = reelRadius * 0.9f

                            val x1 = center.x + (startRadius * cos(rad)).toFloat()
                            val y1 = center.y + (startRadius * sin(rad)).toFloat()
                            val x2 = center.x + (endRadius * cos(rad)).toFloat()
                            val y2 = center.y + (endRadius * sin(rad)).toFloat()

                            drawLine(
                                color = Color(0xFF888888),
                                start = Offset(x1, y1),
                                end = Offset(x2, y2),
                                strokeWidth = 2f
                            )
                        }
                    }
                } else {
                    // 静止状態の歯車
                    for (i in 0 until 12) {
                        val angle = (i * 30).toFloat()
                        val rad = Math.toRadians(angle.toDouble())
                        val startRadius = reelRadius * 0.35f
                        val endRadius = reelRadius * 0.9f

                        val x1 = center.x + (startRadius * cos(rad)).toFloat()
                        val y1 = center.y + (startRadius * sin(rad)).toFloat()
                        val x2 = center.x + (endRadius * cos(rad)).toFloat()
                        val y2 = center.y + (endRadius * sin(rad)).toFloat()

                        drawLine(
                            color = Color(0xFF888888),
                            start = Offset(x1, y1),
                            end = Offset(x2, y2),
                            strokeWidth = 2f
                        )
                    }
                }
            }

            // 中央のテープ部分
            drawRect(
                color = Color(0xFF3D2817),
                topLeft = Offset(width * 0.35f, height * 0.48f),
                size = Size(width * 0.3f, height * 0.04f)
            )

            // ネジ穴（4隅）
            val screwPositions = listOf(
                Offset(width * 0.08f, height * 0.12f),
                Offset(width * 0.92f, height * 0.12f),
                Offset(width * 0.08f, height * 0.88f),
                Offset(width * 0.92f, height * 0.88f)
            )

            screwPositions.forEach { pos ->
                drawCircle(
                    color = Color(0xFF666666),
                    radius = width * 0.015f,
                    center = pos
                )
            }

            // 下部の穴（5つ）
            for (i in 0 until 5) {
                val x = width * (0.3f + i * 0.1f)
                val y = height * 0.85f
                drawCircle(
                    color = Color(0xFF000000),
                    radius = width * 0.02f,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CassetteViewPreview() {
    WoodyTheme {
        CassetteView(
            isPlaying = false,
            modifier = Modifier.fillMaxSize(0.8f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CassetteViewPlayingPreview() {
    WoodyTheme {
        CassetteView(
            isPlaying = true,
            modifier = Modifier.fillMaxSize(0.8f)
        )
    }
}
