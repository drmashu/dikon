package com.github.drmashu.buri.template

/**
 * Created by tnagasaw on 2015/08/13.
 */
public interface Renderer {
    fun render(vararg args: Any) : String
}