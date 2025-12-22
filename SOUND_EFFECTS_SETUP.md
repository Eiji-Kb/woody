# 効果音セットアップガイド

このアプリは効果音システムを実装していますが、著作権の関係で効果音ファイル自体は含まれていません。
以下の手順に従って、効果音ファイルを追加してください。

## 必要な効果音ファイル

以下の6つの効果音ファイルが必要です：

1. **button_click.wav** - ボタンクリック音（カチッという音）
2. **play_start.wav** - 再生開始音
3. **pause.wav** - 一時停止音
4. **stop.wav** - 停止音
5. **mechanical.wav** - メカニカル音（オートリバース時のカセットのメカ音）
6. **eject.wav** - イジェクト音

## 効果音ファイルの入手方法

### 推奨：フリー効果音サイト

以下のサイトから無料で効果音をダウンロードできます：

#### 1. 効果音ラボ（日本語）
- URL: https://soundeffect-lab.info/
- カテゴリ:
  - ボタンクリック音: 「ボタン・カーソル」カテゴリ
  - メカニカル音: 「機械」カテゴリ
  - 再生/停止音: 「システム」カテゴリ

#### 2. DOVA-SYNDROME（日本語）
- URL: https://dova-s.jp/se/
- 「効果音」から検索

#### 3. Freesound（英語）
- URL: https://freesound.org/
- 検索ワード例:
  - "button click"
  - "cassette mechanical"
  - "play button"
  - "stop sound"

### 自分で録音する

- スマートフォンの録音アプリを使用
- カセットプレイヤーの実際のボタン音を録音
- WAV または MP3 形式で保存

## ファイルの配置方法

### 1. ファイル形式の変換（必要な場合）

ダウンロードしたファイルがMP3の場合、そのまま使用できます。
WAV形式を推奨しますが、MP3でも動作します。

### 2. ファイル名の変更

ダウンロードした効果音ファイルの名前を以下のように変更してください：

```
ダウンロードしたファイル → 変更後のファイル名
--------------------------------------------
任意の名前.wav/mp3 → button_click.wav
任意の名前.wav/mp3 → play_start.wav
任意の名前.wav/mp3 → pause.wav
任意の名前.wav/mp3 → stop.wav
任意の名前.wav/mp3 → mechanical.wav
任意の名前.wav/mp3 → eject.wav
```

### 3. ファイルの配置

効果音ファイルを以下のディレクトリに配置します：

```
Woody/
└── app/
    └── src/
        └── main/
            └── res/
                └── raw/     ← ここに配置
                    ├── button_click.wav
                    ├── play_start.wav
                    ├── pause.wav
                    ├── stop.wav
                    ├── mechanical.wav
                    └── eject.wav
```

### 4. コードの有効化

`SoundEffectManager.kt` ファイルを開き、`loadSounds()` メソッド内のコメントを外します：

```kotlin
private fun loadSounds() {
    // この部分のコメントを外す
    try {
        soundMap[SoundEffect.BUTTON_CLICK] = soundPool?.load(context, R.raw.button_click, 1) ?: 0
        soundMap[SoundEffect.PLAY_START] = soundPool?.load(context, R.raw.play_start, 1) ?: 0
        soundMap[SoundEffect.PAUSE] = soundPool?.load(context, R.raw.pause, 1) ?: 0
        soundMap[SoundEffect.STOP] = soundPool?.load(context, R.raw.stop, 1) ?: 0
        soundMap[SoundEffect.MECHANICAL] = soundPool?.load(context, R.raw.mechanical, 1) ?: 0
        soundMap[SoundEffect.EJECT] = soundPool?.load(context, R.raw.eject, 1) ?: 0
    } catch (e: Exception) {
        Log.e(TAG, "Error loading sounds: ${e.message}")
    }
}
```

### 5. ビルドと実行

1. Android Studioで「Build > Rebuild Project」を実行
2. アプリを実行
3. ボタンを押すと効果音が再生されます

## 効果音の仕様

- **ファイル形式**: WAV または MP3
- **サンプルレート**: 44.1kHz 推奨
- **ビットレート**: 128kbps 以上推奨
- **長さ**: 0.1秒～1秒程度（短めが推奨）
- **ファイルサイズ**: 各ファイル 100KB 以下推奨

## トラブルシューティング

### 効果音が再生されない場合

1. **ファイル名を確認**
   - 拡張子まで正確に一致しているか確認
   - 小文字で入力されているか確認

2. **ファイルの配置場所を確認**
   - `app/src/main/res/raw/` に配置されているか確認

3. **コードのコメントアウトを確認**
   - `SoundEffectManager.kt` のコメントが外れているか確認

4. **ビルドをクリーン**
   ```
   Build > Clean Project
   Build > Rebuild Project
   ```

5. **Logcatを確認**
   - Android Studioの Logcat で "SoundEffectManager" でフィルタ
   - エラーメッセージを確認

## 効果音の無効化

効果音を一時的に無効にしたい場合：

```kotlin
// ViewModelまたはActivityで
soundEffectManager.setEnabled(false)  // 無効化
soundEffectManager.setEnabled(true)   // 有効化
```

## 推奨効果音の例

### カセットテープらしい効果音

1. **button_click.wav**: カチッという機械的なクリック音
2. **play_start.wav**: ガシャッというカセット挿入音
3. **pause.wav**: カチッという軽いクリック音
4. **stop.wav**: ガチャンという停止音
5. **mechanical.wav**: ウィーンというモーター音
6. **eject.wav**: シュコンというイジェクト音

効果音を配置したら、レトロなカセットプレイヤーの雰囲気を楽しめます！
