# buri

**buri** is "Bind action to URI".
**buri** is Web Framework for Dikon written in Kotlin.
**buri** is Japanese amberjack.

ぶり大根って言いたいだけでこの名前にしました。

DIコンテナである[Dikon](../README.md)に組み合わせる、Web用の(M)VCフレームワークです。

## テンプレートエンジン
テンプレートエンジンとして、[BuriTeri](../buriteri/README.md)を用意しています。

BuriTeriについては別途。

## 設定
BuriもDikon同様設定ファイルはなく、コンストラクタにMapを設定します。

ファクトリについては、Dikonの説明文を参照してくだい。

キーにオブジェクト名の代わりにパスを指定することで、そのパスへアクセスされた際にMapで割り当てたアクションを呼びます。

アクションはコンストラクタインジェクションを行う必要があるため、
必ずInjectionファクトリを使用し、またSigletonファクトリを使用してはなりません。

また、パスは正規表現を使用でき、名前付きグループを指定することで、
そのグループに該当する値をその名前でコンストラクタにインジェクションします。

    Buri(mapOf(
        Pair("/", Injection(startpage::class)),　// 通常の割り当て
        Pair("/(?<id>[a-z0-9_@$]+)", Injection(Content::class)), // Contentにidをインジェクションする
        Pair("/content", Injection(content::class))
    ))

## 今後の予定

+ FormDTOをView(HTML)とViewModel間でやりとりする仕組み
+ Ajax
+ RPC
+ バインディングを型安全にしたいけど、DIとの相性は悪いだろうなぁ
