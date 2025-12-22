# Woody Cassette Player

レトロなカセットテープ音楽プレイヤーアプリ

## 機能

- 📼 カセットテープの外観を再現したUI ✅
- 🎵 音楽再生機能（ExoPlayerベース） ✅
- ⚙️ リールの回転アニメーション ✅
- 🎮 再生コントロール（再生/一時停止/停止/次/前） ✅
- 📋 プレイリスト表示と選曲 ✅
- 🔊 効果音システム ✅ (音声ファイルは別途配置が必要)
- 🎨 複数のカセットデザインスキン（今後実装予定）
- 🎛️ イコライザー機能（今後実装予定）

## 技術スタック

- **言語**: Kotlin
- **UIフレームワーク**: Jetpack Compose
- **音楽再生**: Media3 (ExoPlayer)
- **依存性注入**: Hilt
- **アーキテクチャ**: MVVM + Clean Architecture

## ビルド要件

- Android Studio Hedgehog | 2023.1.1以上
- JDK 17
- Android SDK 34
- Minimum SDK: 26 (Android 8.0)

## セットアップ

1. Android Studioでプロジェクトを開く
2. Gradle Syncを実行
3. エミュレータまたは実機で実行
4. **（オプション）効果音を有効にする**: [SOUND_EFFECTS_SETUP.md](SOUND_EFFECTS_SETUP.md) を参照

## プロジェクト構造

```
app/
├── src/main/
│   ├── java/com/woody/cassetteplayer/
│   │   ├── data/            # データレイヤー
│   │   │   ├── model/       # データモデル
│   │   │   └── repository/  # リポジトリ
│   │   ├── di/              # 依存性注入モジュール
│   │   ├── player/          # 音楽プレイヤー
│   │   ├── service/         # バックグラウンド再生サービス
│   │   ├── sound/           # 効果音マネージャー
│   │   ├── ui/
│   │   │   ├── components/  # 再利用可能なUIコンポーネント
│   │   │   ├── screen/      # 画面
│   │   │   ├── theme/       # テーマとスタイル
│   │   │   └── viewmodel/   # ViewModel
│   │   ├── MainActivity.kt
│   │   └── WoodyApplication.kt
│   └── res/
│       ├── drawable/        # アイコン等
│       ├── raw/             # 効果音ファイル（別途配置）
│       └── values/          # 文字列、色等
```

## 実装状況

### 完了 ✅
- [x] 音楽ファイルの読み込み
- [x] 再生コントロール（再生/一時停止/停止/次/前）
- [x] プレイリスト機能
- [x] リールの回転アニメーション
- [x] 効果音システム（音声ファイルは別途配置）

### 今後の実装予定
- [ ] カセットスキンの切り替え
- [ ] ウォークマンスキン
- [ ] イコライザー
- [ ] カセットラベルのカスタマイズ
- [ ] シーク機能（曲の途中から再生）
- [ ] リピート/シャッフル機能

## ライセンス

Private Project
