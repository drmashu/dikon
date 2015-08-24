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
        private val blank = """[ \t\r\n]"""
        private val NOT_BLANK = Regex("""[^ \t\r\n]""")

        private val FIRST_LINE = Regex("""@\(([^)]*)\)""")
        private val IF = Regex("${blank}*if${blank}*")
        private val FOR = Regex("${blank}*for${blank}*")
//        private val DECL_FUNCTION = Regex("${blank}*fun (.+)${blank}*")
//        private val FUNCTION = Regex("${blank}*(.+)${blank}*")
        private val ELSE = Regex("${blank}*else${blank}*")
        private val ELSE_IF = Regex("${blank}*else${blank}+if${blank}*")

        private val at = '@'.toInt()
        private val block_start = '{'.toInt()
        private val block_end = '}'.toInt()
        private val lineCommentMark = '/'.toInt()
        private val cr = '\r'.toInt()
        private val nl = '\n'.toInt()
        private val blockCommentMark = '*'.toInt()
        private val bracket_start = '('.toInt()
        private val bracket_end = ')'.toInt()
    }
    /**
     * ディレクトリ中の全ファイルをプリコンパイルする。
     * @param packageName パッケージ名
     * @param _srcDir 対象ディレクトリ
     * @param _distDir 出力先ディレクトリ
     */
    fun precompileAll(packageName: String, _srcDir: String, _distDir: String) {
        val srcDir = File(_srcDir)
        val distDir = File(_distDir)
        if (!srcDir.exists() || !srcDir.canRead()) {
            // 入力ディレクトリがなければエラー
            throw FileNotFoundException() // TODO どんな例外にするか・・・
        }
        walkDir(packageName, srcDir, distDir)
    }

    /**
     * ディレクトリ内のすべてのファイルを対象にする
     * @param packageName パッケージ名
     * @param srcDir 対象ディレクトリ
     * @param distDir 出力先ディレクトリ
     */
    private fun walkDir(packageName: String, srcDir: File, distDir: File) {
        if (!distDir.exists()) {
            // 出力先がなければ作る
            distDir.mkdirs()
        }
        for (file in srcDir.listFiles()) {
            if(file.isDirectory()) {
                walkDir(packageName + "." + file.getName(), file, File(distDir, file.getName()))
            } else {
                precompile(packageName, file, distDir)
            }
        }
    }

    /**
     * 指定されたファイルをプリコンパイルする。
     * 対象のファイルが".kt.html"で終わっていない場合は、無視する。
     * @param packageName パッケージ名
     * @param srcFile 対象ファイル
     * @param distDir 出力先ディレクトリ
     */
    fun precompile(packageName:String, srcFile: File, distDir: File) {
        val name = srcFile.name
        if (name.endsWith(".kt.html", true)) {
            val reader = FileReader(srcFile)
            val distFile = File(distDir, name.substring(0, name.length() - 5))
            val writer = FileWriter(distFile)
            val className = name.substring(0, name.length() - 8)
            precompile(reader, writer, packageName, className)
        }
    }

    /**
     * プリコンパイル処理
     * @param reader 入力元バッファ
     * @param writer 出力先バッファ
     * @param className 出力クラス名
     */
    fun precompile(_reader: Reader, writer: Writer, packageName:String, className: String) {

        var reader = LineNumberReader(_reader)
        val firstLine :String? = reader.readLine()
        var lineIdx = 1

        //一行目専用処理
        var param: String? = null
        // 一行目の先頭が"@"で始まっていたら、このレンダラーのパラメータが指定されるということ
        if (firstLine != null && firstLine.startsWith("@")) {
            // カッコ内をとりだして、レンダラーメソッドのパラメータにする
            val match = FIRST_LINE.match(firstLine)
            param = match?.groups?.get(1)?.value
        } else {
            // TODO 先頭行がパラメータ指定で始まっていないとエラー
            throw Exception()
        }
        if (param == null) {
            //取り出せなければ、パラメータは空
            param = ""
        } else if (!param.isEmpty()) {
            param = ", " + param
        }

        //先頭のコメント
        writer.write("/** Generate source code by Buri Template PreCompiler at ${Date()} */\n")
        writer.write("import java.util.*\n")
        writer.write("import java.io.Writer\n")
        writer.write("import com.github.drmashu.buri.Renderer\n")

        // クラス名
        writer.write("class $className(___writer___: Writer$param) : Renderer(___writer___) {\n")

        //
        writer.write("\tpublic override fun render() {\n")
        val mode = Stack<Mode>()
        // インサートモード
        val insert = object : Mode(writer) {
            override fun process(char: Int) {
                when(char) {
                    block_end -> {
                        writer.write("}\"))\n")
                        // インサートモードから抜ける
                        mode.pop()
                        // マジックモードから抜ける
                        mode.pop()
                    }
                    else -> writer.write(char)
                }
            }
        }
        // 条件モード
        val conditions = object : Mode(writer) {
            override fun process(char: Int) {
                writer.write(char)
                when(char) {
                    bracket_end -> {
                        // モードから抜ける
                        mode.pop()
                    }
                    bracket_start -> {
                        mode.push(this)
                    }
                }
            }
        }
        // コマンドモード
        val command = object : Mode(writer) {
            var buf: StringBuffer? = null
//            var needBlock = false
            override fun process(char: Int) {
                if (buf == null) {
                    buf = StringBuffer()
                }
                when(char) {
                    at -> {
                        if (buf != null && buf!!.length() > 0) {
                            buf = null
                            // コマンドのパラメータ/ブロック開始の前に @ があるとエラー
                            throw BuriLexicalException("$lineIdx : @ の出現位置が不正です")
                        }
//                        needBlock = false
                        // コマンドモードから抜ける
                        mode.pop()
                        // マジックモードから抜ける
                        mode.pop()
                    }
                    bracket_start -> {
                        val commandName = buf.toString()
                        buf = null
//                        needBlock = true
                        if (IF.matches(commandName)) {
                            writer.write("/* $lineIdx */if")
                        } else if (FOR.matches(commandName)) {
                            writer.write("/* $lineIdx */for")
                        } else {
//                            val matched = DECL_FUNCTION.match(commandName)
//                            if (matched != null) {
//                                // 関数定義の場合
//                                val funcId = matched.groups.get(1)!!.value
//                                if (!funcId.isIdentifier()) {
//                                    // TODO 識別子のルールに合致しない
//                                    throw Exception()
//                                }
//                                writer.write("/* $lineIdx */fun $funcId")
//                            } else {
//                                val matched2 = FUNCTION.match(commandName)
//                                if (matched2 != null) {
//                                    val funcId = matched2.groups.get(1)!!.value
//                                    if (!funcId.isIdentifier()) {
//                                        // TODO 識別子のルールに合致しない
//                                        throw Exception()
//                                    }
//                                    writer.write("/* $lineIdx */$funcId")
//                                } else {
//                                    // TODO 定義済み関数呼び出しでもない場合はエラー
//                                    throw Exception()
//                                }
//                                needBlock = false
//                            }
                            // 提供されているコマンド以外の場合はエラー
                            throw BuriLexicalException("$lineIdx : 不正なコマンドです")
                        }
                        writer.write("(")
                        mode.push(conditions)
                    }
                    block_start -> {
//                        if (!needBlock) {
//                            // TODO needBlock がtrueになるルートからでなければ
//                            throw Exception()
//                        }
                        if (buf != null && NOT_BLANK.matches(buf!!.toString())) {
                            throw BuriLexicalException("$lineIdx : ブロック開始の前に不正な文字があります")
                        }
                        writer.write(" {\n")
                        buf = null;
//                        val next = reader.read()
//                        if(next != at) {
//                            // TODO 次は at 以外は許さない
//                            throw Exception()
//                        }
                    }
                    else -> {
                        buf?.append(char.toChar())
                    }
                }
            }
        }
        // ブロック終了モード
        val blockEnd = object : Mode(writer) {
            var buf: StringBuffer? = null
            var elif = false
            override fun process(char: Int) {
                if (buf == null) {
                    buf = StringBuffer()
                }
                when(char) {
                    at -> {
                        if (elif) {
                            // else if の後 { の前に @ が出現してはダメ
                            throw BuriLexicalException("$lineIdx : @ の出現位置が不正です")
                        }
                        if (0 != buf?.length()) {
                            // なんらかのコマンドがあるのに@が出現している
                            throw BuriLexicalException("$lineIdx : @ の出現位置が不正です")
                        }
                        writer.write("/* $lineIdx */}\n")
                        // @で終了
                        buf = null
                        elif = false
                        // ブロック終了から抜ける
                        mode.pop()
                        // マジックモードから抜ける
                        mode.pop()
                    }
                    bracket_start -> {
                        val commandName = buf.toString()
                        if (!ELSE_IF.matches(commandName)) {
                            elif = false
                            // else if以外は許さない
                            throw BuriLexicalException("$lineIdx : 条件パラメータの出現位置が不正です")
                        }
                        writer.write("/* $lineIdx */} else if (")
                        mode.push(conditions)
                        buf = null
                        elif = true
                    }
                    block_start -> {
                        try {
                            val commandName = buf.toString()
                            val isElse = ELSE.matches(commandName)
                            if (!elif && !isElse) {
                                // else か else if 以外は許さない
                                throw BuriLexicalException("$lineIdx : 不正なコマンドです")
                            }
                            if (isElse) {
                                writer.write("/* $lineIdx */} else ")
                            }
                            writer.write("{\n")
                            if(reader.read() != at) {
                                // 次は at 以外は許さない
                                throw BuriLexicalException("$lineIdx : 不正な文字があります")
                            }
                            // ブロック終了から抜ける
                            mode.pop()
                            // マジックモードから抜ける
                            mode.pop()
                        } finally {
                            buf = null
                            elif = false
                        }
                    }
                    else -> {
                        buf?.append(char.toChar())
                    }
                }
            }
        }
        // 行コメントモード
        val lineComment = object : Mode(writer) {
            override fun process(char: Int) {
                when(char) {
                    cr , nl -> {
                        if (char == cr) {
                            // CRの次はNLが来るので読み飛ばす
                            reader.read()
                        }
                        // 行コメントから抜ける
                        mode.pop()
                        // マジックモードから抜ける
                        mode.pop()
                        writer.write("/* $lineIdx */___writer___.write(\"\"\"")
                    }
                }
            }
        }
        // ブロックコメントモード
        val blockComment = object : Mode(writer) {
            var commentEnd = false
            override fun process(char: Int) {
                when(char) {
                    blockCommentMark -> commentEnd = true
                    at -> {
                        if (commentEnd) {
                            // ブロックコメントから抜ける
                            mode.pop()
                            // マジックモードから抜ける
                            mode.pop()
                        }
                    }
                    else -> commentEnd = false
                }
            }
        }
        // マジックモード
        val magic = object : Mode(writer) {
            override fun process(char: Int) {
                when(char) {
                    block_start -> {
                        // インサート
                        mode.push(insert)
                        writer.write("/* $lineIdx */___writer___.write(encode(\"\${")
                    }
                    at -> {
                        // エスケープ
                        writer.write("/* $lineIdx */___writer___.write(\"@\")\n")
                        mode.pop()
                    }
                    lineCommentMark -> {
                        // 行コメント
                        mode.push(lineComment)
                    }
                    blockCommentMark -> {
                        // ブロックコメント
                        mode.push(blockComment)
                    }
                    block_end -> {
                        // ブロック終了
                        mode.push(blockEnd)
                    }
                    else -> {
                        // ID
                        mode.push(command)
                        command.process(char)
                    }
                }
            }
        }
        // ノーマルモード
        val normal = object : Mode(writer) {
            var inMode = false
            override fun process(char: Int) {
                when(char) {
                    at -> {
                        writer.write("\"\"\")\n")
                        mode.push(magic)
                        inMode = true
                    }
                    else -> {
                        if (inMode) {
                            writer.write("/* $lineIdx */___writer___.write(\"\"\"")
                            inMode = false
                        }
                        writer.write(char)
                    }
                }
            }
        }
        mode.push(normal)

        writer.write("/* $lineIdx */___writer___.write(\"\"\"")
        while(reader.ready()) {
            var char = reader.read()
            if (char == -1) {
                break;
            }
            if (char == cr) {
                char = reader.read()
            }
            if (char == nl) {
                lineIdx++
            }
            mode.peek().process(char)
        }
        writer.write("\"\"\")\n")
        writer.write("\t}\n")
        writer.write("}\n")
    }

    abstract class Mode(val writer: Writer) {
        abstract fun process(char: Int);
    }
}

///**
// * Javaの識別子として使える文字列かどうかをチェックする
// */
//fun String.isIdentifier() : Boolean {
//    if (!this[0].isJavaIdentifierStart()) {
//        return false
//    }
//    var result = true
//    this.forEachIndexed { i, c ->
//        if (i > 0) {
//            result = result && c.isJavaIdentifierPart()
//        }
//    }
//    return result
//}

/**
 *
 */
fun main(args: Array<String>) {
    if (args.size() < 2) {
        // TODO エラー
        return
    }
    try {
        PreCompiler().precompileAll(args[0], args[1], args[2])
    } catch (e: FileNotFoundException) {
        // TODO エラー
    }
}
