package com.github.drmashu.buri.template

import org.apache.tools.ant.BuildException
import org.apache.tools.ant.Task

/**
 * BuriTemplateのant タスク
 */
public class BuriTemplateTask: Task() {
    var srcDir: String? = null
    var distDir: String? = null
    var packageName: String? = null

    /**
     * BuriTemplateをプリコンパイルする
     */
    public override fun execute() {
        if (srcDir == null) {
            throw BuildException("srcDir is required.")
        }
        if (distDir == null) {
            throw BuildException("distDir is required.")
        }
        if (packageName == null) {
            packageName = ""
        }
        try {
            PreCompiler().precompileAll(packageName!!, srcDir!!, distDir!!)
        } catch (e: BuriLexicalException) {
            this.log(e.getMessage())
        }
    }
}