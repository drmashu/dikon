package com.github.drmashu.buri

import com.github.drmashu.dikon.Dikon
import com.github.drmashu.dikon.Factory
import com.github.drmashu.dikon.Holder
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
public abstract class Action(val request: HttpServletRequest, val response: HttpServletResponse) {
    var ___buri: Buri? = null
    val writer: Writer
        get() = response.writer
    private val dikon: Dikon
        get() = ___buri!!.dikon
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
    public open fun put() { get() }
    /**
     * DELETE Methodに該当する処理.
     */
    public open fun delete() { get() }
    /**
     * 各言語ごとのエスケープ処理を実装する.
     */
    protected abstract fun encode(str :String) :String
    /**
     * リダイレクト
     */
    protected fun redirect(name: String, vararg args: Pair<String, Any>) {
        val factory = dikon.objectMap.get(name)
        val paramMap: MutableMap<String, Factory<*>> = hashMapOf(
                Pair("request", Holder(request)),
                Pair("response", Holder(response))
        )
        for (arg in args) {
            var value =
            if (arg.second is Factory<*>) {
                arg.second as Factory<*>
            } else {
                Holder(arg.second)
            }
            paramMap.put(arg.first, value)
        }
        ___buri!!.callAction(factory!!, paramMap, request)
    }
    protected fun responseByJson(result: Any) {

    }
}
