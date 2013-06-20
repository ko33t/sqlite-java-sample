# sqlite3をJavaで使う

わりとよく使うからメモ

###eclipseの場合
1. パッケージエクスプローラのプロジェクト名が書いてあるところで右クリック->プロパティ
2. Javaのビルドパス->ライブラリタブを開く
3. libs内の該当jar追加してやる

### IDEAの場合
1. File -> Project Structure -> Modules
2. 該当モジュールのDependenciesでjarを追加
3. ScopeをRuntimeに指定してやる