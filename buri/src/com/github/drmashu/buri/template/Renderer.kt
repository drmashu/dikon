package com.github.drmashu.buri.template

import java.io.Writer

/**
 * Created by tnagasaw on 2015/08/13.
 */
public abstract class Renderer(val ___writer___: Writer) {
    public abstract fun render()
}