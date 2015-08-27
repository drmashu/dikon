package com.github.drmashu.buri

import java.io.Writer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * アクションのスーパークラス.
 * アクションはコンストラクタインジェクションをされるので、
 * 継承するクラスでwriter、request、responseを使用したい場合は、
 * コンストラクタ引数の名称を変えてはならない。
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 */
public abstract class Action(val writer: Writer, val request: HttpServletRequest, val response: HttpServletResponse) {
    /**
     * GET Methodに該当する処理.
     */
    public open fun get() {}
    /**
     * POST Methodに該当する処理.
     */
    public open fun post() { get() }
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