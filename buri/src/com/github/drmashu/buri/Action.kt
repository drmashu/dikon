package com.github.drmashu.buri

import java.io.Writer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * レンダラーのスーパークラス
 */
public abstract class Action(val ___writer___: Writer, val request: HttpServletRequest, val response: HttpServletResponse) {
    /**
     * GET Methodに該当する処理.
     */
    public open fun get() {}
    /**
     * POST Methodに該当する処理.
     */
    public open fun post() {}
    /**
     * PUT Methodに該当する処理.
     */
    public open fun put() {}
    /**
     * DELETE Methodに該当する処理.
     */
    public open fun delete() {}
    /**
     * 各言語ごとのエスケープ処理を実装する.
     */
    protected abstract fun encode(str :String) :String
}