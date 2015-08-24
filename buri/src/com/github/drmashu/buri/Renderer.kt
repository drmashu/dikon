package com.github.drmashu.buri

import java.io.Writer

/**
 * Created by tnagasaw on 2015/08/13.
 */
public abstract class Renderer(val ___writer___: Writer) {
    public abstract fun render()
    protected fun encode(str :String) :String {
        var result = str
        result = result.replace("<", "&lt;")
        result = result.replace(">", "&gt;")
        result = result.replace("&", "&amp;")
        result = result.replace("\"", "&quot;")
        result = result.replace("'", "&#39;")
        return result
    }
}