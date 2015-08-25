package com.github.drmashu.buri

import java.io.Writer

/**
 * Created by tnagasaw on 2015/08/13.
 */
public abstract class Renderer(val ___writer___: Writer) {
    /**
     * テンプレート変換処理
     */
    public abstract fun render()
    /**
     * 埋め込まれる文字列をHTMLに影響しない文字に変換
     */
    protected fun encode(str :String) :String {
        var buf = StringBuffer()
        for(ch in str) {
            buf.append(
                when(ch) {
                    '<' ->  "&lt;"
                    '>' ->  "&gt;"
                    '&' ->  "&amp;"
                    '"' ->  "&quot;"
                    '\'' ->  "&#39;"
                    else ->  ch
                }
            )
        }
        return buf.toString()
    }
}