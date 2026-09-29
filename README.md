# miniCV00
miniCV00 for expB on Faculty of Informatics, Shizuoka University.

## 実行の仕方

[実行とデバッグ] から下記適切なものを選ぶこと

- TestCToken : c\test.txt の字句解析を実行
- TC to File : c\test.txt を軸解析し，結果を c\tokenizerResult.txt に保存
- MiniCompiler : c\test.txt をコンパイルする
- MC no comment : c\test.txt をコンパイルする(単体コメント行は表示しない)"
- MC output **.s" : c\test.txt のコンパイル結果を，ファイルに保存

## アセンブルの方法

ターミナルを起動する(Powershell ではなく，Command Prompt にする)．詳細は `simuTest/readme.html` を参照のこと

1. `cd simuTest` を実行してカレントディレクトリを変更する．
2. `asm.bat cv??\???.s` でアセンブルを実行する．`bin` ファイルは，`*.s` ファイルが有るフォルダに保存される
3. `simu.bat` でシミュレータが起動できる．電源を入れたあと，`cv??\*.bin` を読み込むこと．