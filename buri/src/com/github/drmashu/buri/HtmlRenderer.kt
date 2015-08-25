package com.github.drmashu.buri

import java.io.Writer

/**
 * Created by 貴博 on 2015/08/25.
 */
public abstract class HtmlRenderer(___writer___: Writer): Renderer(___writer___){
    protected override fun encode(str :String) :String {
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