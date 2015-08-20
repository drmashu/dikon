package com.github.drmashu.buri.template

import com.github.drmashu.buri.template.parser.BuriBaseListener
import com.github.drmashu.buri.template.parser.BuriLexer
import com.github.drmashu.buri.template.parser.BuriParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.*
import java.util.*

/**
 * テンプレートプリコンパイラ.
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 */
public class PreCompiler {
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
            if(file.isDirectory()) {
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
        //先頭のコメント
        writer.write("/** Generate source code by Buri Template PreCompiler at ${Date()}*/\n")
        // クラス名
        writer.write("class $className : Renderer {\n")
        val lexer = BuriLexer(ANTLRInputStream(reader))
        val parser = BuriParser(CommonTokenStream(lexer))

        val listener = object : BuriBaseListener() {
            override fun enterTemplate(ctx: BuriParser.TemplateContext) {
                val param = ctx.start.getText()
                writer.write("\tfun override render(${param.substring(2, param.length()-1)}) : String {\n")
                writer.write("\t\tvar ___buffer = StringBuffer()\n")
            }
            override fun enterBody(ctx: BuriParser.BodyContext) {
                ctx.start.getText()
            }
            override fun exitBody(ctx: BuriParser.BodyContext) {
                ctx.stop.getText()
            }
            override fun exitText(ctx: BuriParser.TextContext) {
                writer.write("/* ${ctx.start.getLine()} */___buffer.append(\"\"\"")
                for(elem in ctx.children) {
                    writer.write(if (elem.getText() == "@@") "@" else elem.getText())
                }
                writer.write("\"\"\")\n")
            }
            override fun enterElemInsert(ctx: BuriParser.ElemInsertContext) {
                val text = ctx.start.getText()
                writer.write("/* ${ctx.start.getLine()} */___buffer.append( ${text.substring(2, text.length() - 1)} )\n")
            }
            override fun enterElemIf(ctx: BuriParser.ElemIfContext) {
                val text = ctx.start.getText()
                writer.write("/* ${ctx.start.getLine()} */if ")
            }
            override fun enterElemElseIf(ctx: BuriParser.ElemElseIfContext) {
                writer.write("/* ${ctx.start.getLine()} */else if ")
            }
            override fun enterElemElse(ctx: BuriParser.ElemElseContext) {
                writer.write("/* ${ctx.start.getLine()} */else ")
            }
            override fun enterElemFor(ctx: BuriParser.ElemForContext) {
                writer.write("/* ${ctx.start.getLine()} */for ")
            }
            override fun enterElemBrake(ctx: BuriParser.ElemBrakeContext) {
                writer.write("/* ${ctx.start.getLine()} */break\n")
            }
            override fun enterElemContinue(ctx: BuriParser.ElemContinueContext) {
                writer.write("/* ${ctx.start.getLine()} */continue\n")
            }
            override fun enterConditions(ctx: BuriParser.ConditionsContext) {
                writer.write(ctx.start.getText())
            }
            override fun exitConditions(ctx: BuriParser.ConditionsContext) {
                ctx.stop.getText()
            }

            override fun enterContent(ctx: BuriParser.ContentContext) {
                ctx.start.getText()
            }
            override fun exitContent(ctx: BuriParser.ContentContext) {
                ctx.stop.getText()
            }

            override fun exitTemplate(ctx: BuriParser.TemplateContext) {
                writer.write("\t\treturn ___buffer.toString()\n")
                writer.write("\t}\n")
                writer.write("}\n")
            }
        }
        parser.addParseListener(listener)
        parser.template()
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
