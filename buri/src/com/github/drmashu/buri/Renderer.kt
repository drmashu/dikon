package com.github.drmashu.buri

import java.io.Writer

/**
 * レンダラーのスーパークラス
 */
public abstract class Renderer(val ___writer___: Writer) {
    /**
     * テンプレート変換処理
     */
    public abstract fun render()
    /**
     * 埋め込まれる文字列をHTMLに影響しない文字に変換
     */
    protected abstract fun encode(str :String) :String
}