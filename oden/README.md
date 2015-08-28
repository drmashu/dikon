# Oden

**Oden** is Micro ORM Framework.

DIコンテナである[Dikon](../README.md)に組み合わせる、ORMフレームワークです。

実際のエンジンは既存ORMのものを借りて、薄いラッパーとしてKotlinでの使用、Dikonでの使用に便利なように拡張する感じで。

+ DDL<->DTO生成
+ 値のマッピング
+ 単純なCRUDレベルのコード生成
+ DB操作の(一部)自動化
はやりたい。