package com.github.drmashu.buri

import java.io.Writer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * HTML用のレンダラクラス.
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 * @param ___writer___ 出力先ストリーム
 */
public abstract class HtmlAction(___writer___: Writer, req: HttpServletRequest, res: HttpServletResponse): Action(___writer___, req, res){
    /**
     * エンコード処理
     */
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