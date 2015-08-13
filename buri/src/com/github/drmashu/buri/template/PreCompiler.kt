package com.github.drmashu.buri.template

import java.io.*
import java.util.*
import kotlin.text.Regex

/**
 * テンプレートプリコンパイラ.
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 */
public class PreCompiler {
    companion object {
        private val FIRST_LINE = Regex("@\\(([^)]*)\\)")
        private val COMMAND = Regex("(if|for)(\\([^\\)]*\\))")
    }
    /**
     * ディレクトリ中の全ファイルをプリコンパイルする。
     * @param _srcDir 対象ディレクトリ
     * @param _distDir 出力先ディレクトリ
     */
    fun precompileAll(_srcDir: String, _distDir: String) {
        val srcDir = File(_srcDir)
        val distDir = File(_distDir)
        if (!srcDir.exists() || !srcDir.canRead()) {
            // 入力ディレクトリがなければエラー
            throw FileNotFoundException() // TODO どんな例外にするか・・・
        }
        walkDir(srcDir, distDir)
    }

    /**
     * ディレクトリ内のすべてのファイルを対象にする
     * @param srcDir 対象ディレクトリ
     * @param distDir 出力先ディレクトリ
     */
    private fun walkDir(srcDir: File, distDir: File) {
        if (!distDir.exists()) {
            // 出力先がなければ作る
            distDir.mkdirs()
        }
        for (file in srcDir.listFiles()) {
            if(file.isDirectory) {
                walkDir(file, File(distDir, file.getName()))
            } else {
                precompile(file, distDir)
            }
        }
    }

    /**
     * 指定されたファイルをプリコンパイルする。
     * 対象のファイルが".kt.html"で終わっていない場合は、無視する。
     * @param srcFile 対象ファイル
     * @param distDir 出力先ディレクトリ
     */
    fun precompile(srcFile: File, distDir: File) {
        val name = srcFile.name
        if (name.endsWith(".kt.html", true)) {
            val reader = FileReader(srcFile)
            val distFile = File(distDir, name.substring(0, name.length() - 5))
            val writer = FileWriter(distFile)
            val className = name.substring(0, name.length() - 8)
            precompile(reader, writer, className)
        }
    }

    /**
     * プリコンパイル処理
     * @param reader 入力元バッファ
     * @param writer 出力先バッファ
     * @param className 出力クラス名
     */
    fun precompile(reader: Reader, writer: Writer, className: String) {
        var textReader = LineNumberReader(reader)
        var line :String? = textReader.readLine()
        //先頭のコメント
        writer.write("/** Generate source code by Buri Template PreCompiler at ${Date()}*/\n")
        // クラス名
        writer.write("class $className : Renderer {\n")

        //一行目専用処理
        var param : String? = null
        // 一行目の先頭が"@"で始まっていたら、このレンダラーのパラメータが指定されるということ
        if (line != null && line.startsWith("@")) {
            // カッコ内をとりだして、レンダラーメソッドのパラメータにする
            val match = FIRST_LINE.match(line)
            param = match?.groups?.get(1)?.value
            line = textReader.readLine()
        }
        if (param == null) {
            //取り出せなければ、パラメータは空
            param = ""
        }
        //
        writer.write("\tfun override render($param) : String {\n")
        writer.write("\t\tvar ___buffer = StringBuffer()\n")

        while (line != null) {
            val rowNo = textReader.lineNumber
            writer.write("/* $rowNo */ ___buffer.append(\"")//元ファイルの行番号埋め込み
            var idx = 0
            var brace: Int = 0
            val len = line.length()
            chars@while(idx < len) {
                val ch = line[idx++]
                if (ch == '@') {
                    if (idx + 1 >= len) {
                        // TODO エラー
                        throw Exception("意味のない@がある")
                    }
                    val next = line[idx]
                    when (next) {
                        '@' -> {
                            if (idx + 1 < len && line[idx+1] == '@') {
                                writer.write('@'.toInt())
                                idx++
                            } else {
                                break@chars
                            }
                        }
                        '{' -> {

                        }
                        else -> {

                        }
                    }
                } else {
                    writer.write(ch.toInt())
                }
            }
            line = textReader.readLine()
        }
        writer.write("\t\treturn ___buffer.toString()\n")
        writer.write("\t}\n")
        writer.write("}\n")
    }
}

/**
 *
 */
fun main(args: Array<String>) {
    if (args.size() < 2) {
        // TODO エラー
        return
    }
    try {
        PreCompiler().precompileAll(args[0], args[1])
    } catch (e: FileNotFoundException) {
        // TODO エラー
    }
}
