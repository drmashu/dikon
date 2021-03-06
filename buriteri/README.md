# BuriTeri

**BuriTeri** is "buri TEmplate precompiler".

[Buri](../buri/README.md)のテンプレートエンジン、BuriTeriです。

テンプレートはBuriのActionクラスに変換され、getメソッドとして実装されます。

いまのところ、テンプレートを静的にKotlinのソースに変換するところまでしか実装していません。

いずれはテンプレートをデプロイすると動的にクラスファイルができるような仕組みも作りたいですが、
優先度は低いです。

## BuriTeriテンプレートの書式

Play Frameworkのテンプレートに似せて作りましたが、
まだフル機能ではないですし、
テンプレートに使用する言語要素とBuriTeriの言語要素を自動で判別するようには作っていません。
(Razorのように元言語をhtmlに限定し、タグで囲まれた部分をhtml扱いするのであればできると思いますが、
javascript混じりにできませんし、jsonをテンプレートにすることもできないので、
自動判別をやるつもりはいまのところありません)

### 基本の「@」
「@」から「@」までをBuriTeri要素として扱います。

例外はテンプレート引数、インポート文(未実装)、行コメント、埋め込み文です。

### テンプレート引数
テンプレート引数は、ファイルの先頭行に必ず書く必要が有ります。

クラス宣言の代わりに@と書く以外はKotlinの関数等に引数を記述するのと変わりません。

    @(id:String, value:Int)

先頭行は必ずテンプレート引数として扱うため、BuriTeri要素終了の「@」は必要ありません。

また、引数が必要ない場合も

    @()

のような記述が必要です。

BuriのActionクラスはdikonによってコンストラクターインジェクションされるため、
テンプレート引数もコンストラクター引数になります。

### インポート文(未実装)
テンプレート引数の次の行からインポート文を書くことができます。

    @import xxx.yyy.zzz.*

これも行単位で扱うため、、BuriTeri要素終了の「@」は必要ありません。
    
インポート文を書かない場合も「java.util.*」「javax.servlet.http.*」はインポートされます。

### 埋め込み文
「@{」と「}」に囲まれたKotlinの式を解釈し、文字列としてhtmlに埋め込みます。

埋め込みの際には、対象のフォーマットに合わせたエスケープ処理が行われます。

今のところ、埋め込むKotlinの式に「{」や「}」を含ませることを考慮していません。(必要？)

### ループ
for文に対応しています。

値範囲のループや

    <ol>
    @for (idx in 0..10) {@
        <li>@{idx}
    @}@
    </ol>

コレクションの展開に対応しています。

    <ol>
    @for (item in list) {@
        <li>@{item.text}
    @}@
    </ol>

### 条件分岐
if文に対応しています。

    @if (flg1) {@
        <br>
    @}@

elseや else ifで複数分岐可能です。
    
    @if (flg1) {@
        flg1
    @} else if (flg2) {@
        flg2
    @} else {@
        other
    @}@

### コメント

行コメントは以下のように書きます。

    @/ 行コメント


ブロックコメントは以下のように。

    @*
     * ブロックコメント
     *@

ただし、Kotlin式の途中をコメントにすることはできません。

### エスケープ

「@」をテンプレート中に記載したい場合は、「@@」と2つ続けて書いてください。

