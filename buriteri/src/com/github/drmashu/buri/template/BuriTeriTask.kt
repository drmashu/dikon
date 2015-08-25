package com.github.drmashu.buri.template

import org.apache.tools.ant.BuildException
import org.apache.tools.ant.Task
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter

/**
 * BuriTemplateのant タスク
 */
public class BuriTeriTask: Task() {
    var srcdir: String? = null
    var destdir: String? = null
    var packageName: String? = null

    private val precompiler = PreCompiler()

    /**
     * ディレクトリ中の全ファイルをプリコンパイルする。
     * @param packageName パッケージ名
     * @param _srcDir 対象ディレクトリ
     * @param _destDir 出力先ディレクトリ
     */
    fun precompileAll(packageName: String, _srcDir: String, _destDir: String) {
        val srcDir = File(_srcDir)
        val packageDir = packageName.replace(",", "/")
        val distDir = File(_destDir + "/" + packageDir)
        if (!srcDir.exists() || !srcDir.canRead()) {
            // 入力ディレクトリがなければエラー
            throw FileNotFoundException("")
        }
        walkDir(packageName, srcDir, distDir)
    }

    /**
     * ディレクトリ内のすべてのファイルを対象にする
     * @param packageName パッケージ名
     * @param srcDir 対象ディレクトリ
     * @param destDir 出力先ディレクトリ
     */
    private fun walkDir(packageName: String, srcDir: File, destDir: File) {
        for (file in srcDir.listFiles()) {
            if(file.isDirectory()) {
                walkDir(packageName + "." + file.getName(), file, File(destDir, file.getName()))
            } else {
                precompile(packageName, file, destDir)
            }
        }
    }

    /**
     * 指定されたファイルをプリコンパイルする。
     * 対象のファイルが".kt.html"で終わっていない場合は、無視する。
     * @param packageName パッケージ名
     * @param srcFile 対象ファイル
     * @param destDir 出力先ディレクトリ
     */
    fun precompile(packageName:String, srcFile: File, destDir: File) {
        val name = srcFile.name
        if (name.endsWith(".kt.html", true)) {
            if (!destDir.exists()) {
                // 出力先がなければ作る
                destDir.mkdirs()
            }
            this.log("precompile '$name'.")
            val reader = FileReader(srcFile)
            val distFile = File(destDir, name.substring(0, name.length() - 5))
            val writer = FileWriter(distFile)
            val className = name.substring(0, name.length() - 8)
            precompiler.precompile(reader, writer, packageName, className)
            writer.flush()
            writer.close()
            reader.close()
            this.log("output '${distFile.name}'.")
        }
    }

    /**
     * BuriTemplateをプリコンパイルする
     */
    public override fun execute() {
        if (srcdir == null) {
            srcdir = "."
        }
        if (destdir == null) {
            destdir = srcdir
        }
        if (packageName == null) {
            packageName = ""
        }
        try {
            precompileAll(packageName!!, srcdir!!, destdir!!)
        } catch (e: Exception) {
            this.log(e.getMessage())
        }
    }
}
