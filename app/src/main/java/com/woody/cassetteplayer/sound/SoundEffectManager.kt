package com.woody.cassetteplayer.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import com.woody.cassetteplayer.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundEffectManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<SoundEffect, Int>()
    private val loadedSounds = mutableSetOf<Int>()
    private var isEnabled = true

    companion object {
        private const val TAG = "SoundEffectManager"
        private const val MAX_STREAMS = 5
    }

    init {
        initSoundPool()
    }

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(MAX_STREAMS)
            .setAudioAttributes(audioAttributes)
            .build()

        // 読み込み完了リスナーを設定
        soundPool?.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                loadedSounds.add(sampleId)
                Log.d(TAG, "Sound loaded: sampleId=$sampleId, total loaded=${loadedSounds.size}")
            } else {
                Log.e(TAG, "Failed to load sound: sampleId=$sampleId, status=$status")
            }
        }

        loadSounds()
    }

    private fun loadSounds() {
        try {
            soundMap[SoundEffect.BUTTON_CLICK] = soundPool?.load(context, R.raw.button_click, 1) ?: 0
            soundMap[SoundEffect.PLAY_START] = soundPool?.load(context, R.raw.play_start, 1) ?: 0
            soundMap[SoundEffect.PAUSE] = soundPool?.load(context, R.raw.pause, 1) ?: 0
            soundMap[SoundEffect.STOP] = soundPool?.load(context, R.raw.stop, 1) ?: 0
            soundMap[SoundEffect.MECHANICAL] = soundPool?.load(context, R.raw.mechanical, 1) ?: 0
            soundMap[SoundEffect.EJECT] = soundPool?.load(context, R.raw.eject, 1) ?: 0
            Log.d(TAG, "Started loading ${soundMap.size} sound effects...")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading sounds: ${e.message}")
        }
    }

    /**
     * 効果音を再生
     * @param effect 再生する効果音の種類
     * @param volume 音量（0.0f～1.0f）デフォルト1.0f
     */
    fun play(effect: SoundEffect, volume: Float = 1.0f) {
        if (!isEnabled) {
            Log.d(TAG, "Sound effects are disabled")
            return
        }

        val soundId = soundMap[effect]
        Log.d(TAG, "Attempting to play $effect, soundId=$soundId, loaded=${loadedSounds.contains(soundId)}")

        if (soundId != null && soundId > 0) {
            if (loadedSounds.contains(soundId)) {
                val streamId = soundPool?.play(
                    soundId,
                    volume,
                    volume,
                    1,
                    0,
                    1.0f
                )
                Log.d(TAG, "Playing sound effect: $effect (soundId=$soundId, streamId=$streamId, volume=$volume)")
            } else {
                Log.w(TAG, "Sound not yet loaded: $effect (soundId=$soundId)")
            }
        } else {
            Log.e(TAG, "Invalid sound ID for effect: $effect")
        }
    }

    /**
     * 効果音の有効/無効を切り替え
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    /**
     * 効果音が有効かどうか
     */
    fun isEnabled(): Boolean = isEnabled

    /**
     * リソースの解放
     */
    fun release() {
        soundPool?.release()
        soundPool = null
        soundMap.clear()
        loadedSounds.clear()
    }
}
