package com.github.drmashu.buri.template

import java.io.StringReader
import java.io.StringWriter
import java.util.*
import org.junit.Test as test
import org.junit.Before as before
import org.junit.After as after

import kotlin.test.assertEquals

/**
 * Created by tnagasaw on 2015/08/13.
 */
public class PreCompilerTest {
    /**
     *
     */
    test fun testPreCompileEmpty() {
        val compiler = PreCompiler()
        val reader = StringReader("""@()
""")
        val writer = StringWriter(1024)
        compiler.precompile(reader, writer, "test", "test")
        assertEquals("/** Generate source code by Buri Template PreCompiler at ${Date()} */\n"
                + "package test\n"
                + "import java.util.*\n"
                + "import java.io.Writer\n"
                + "import com.github.drmashu.buri.Renderer\n"
                + "class test(___writer___: Writer) : Renderer(___writer___) {\n"
                + "\tpublic override fun render() {\n"
                + "/* 1 */___writer___.write(\"\"\"\"\"\")\n"
                + "\t}\n"
                + "}\n"
                , writer.toString())
        print(writer.toString())
    }
    test fun testPreCompile() {
        val compiler = PreCompiler()
        val reader = StringReader("""@(val list: List<String>)
<!html>
<!-- -->
<html>
<head></head>
<body>
drmashu@@gmail.com
<br>
<ol>
@for(idx in 0..10) {@
    @if(idx % 3 == 0) {@
        <li> san!
    @} else if(idx % 2 == 0) {@
        <li> even
    @} else {@
        <li> odd
    @}@
@}@
@//comment
@for(text in list){@
    <li>@{text}
@}@
</ol>
<input name="aa" value="@{this.toString()}"/>
</body>
</html>
""")
        val writer = StringWriter(1024)
        compiler.precompile(reader, writer, "test", "test")
        assertEquals("/** Generate source code by Buri Template PreCompiler at ${Date()} */\n"
                + "package test\n"
                + "import java.util.*\n"
                + "import java.io.Writer\n"
                + "import com.github.drmashu.buri.Renderer\n"
                + "class test(___writer___: Writer, val list: List<String>) : Renderer(___writer___) {\n"
                + "\tpublic override fun render() {\n"
                + "/* 1 */___writer___.write(\"\"\"<!html>\n"
                + "<!-- -->\n"
                + "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "drmashu\"\"\")\n"
                + "/* 6 */___writer___.write(\"@\")\n"
                + "/* 6 */___writer___.write(\"\"\"gmail.com\n"
                + "<br>\n"
                + "<ol>\n"
                + "\"\"\")\n"
                + "/* 9 */for(idx in 0..10) {\n"
                + "/* 10 */___writer___.write(\"\"\"\n"
                + "    \"\"\")\n"
                + "/* 10 */if(idx % 3 == 0) {\n"
                + "/* 11 */___writer___.write(\"\"\"\n"
                + "        <li> san!\n"
                + "    \"\"\")\n"
                + "/* 12 */} else if (idx % 2 == 0){\n"
                + "/* 13 */___writer___.write(\"\"\"\n"
                + "        <li> even\n"
                + "    \"\"\")\n"
                + "/* 14 */} else {\n"
                + "/* 15 */___writer___.write(\"\"\"\n"
                + "        <li> odd\n"
                + "    \"\"\")\n"
                + "/* 16 */}\n"
                + "/* 17 */___writer___.write(\"\"\"\n"
                + "\"\"\")\n"
                + "/* 17 */}\n"
                + "/* 18 */___writer___.write(\"\"\"\n"
                + "\"\"\")\n"
                + "/* 19 */___writer___.write(\"\"\"\"\"\")\n"
                + "/* 19 */for(text in list) {\n"
                + "/* 20 */___writer___.write(\"\"\"\n"
                + "    <li>\"\"\")\n"
                + "/* 20 */___writer___.write(\"\${text}\")\n"
                + "/* 21 */___writer___.write(\"\"\"\n"
                + "\"\"\")\n"
                + "/* 21 */}\n"
                + "/* 22 */___writer___.write(\"\"\"\n"
                + "</ol>\n"
                + "<input name=\"aa\" value=\"\"\"\")\n"
                + "/* 23 */___writer___.write(\"\${this.toString()}\")\n"
                + "/* 23 */___writer___.write(\"\"\"\"/>\n"
                + "</body>\n"
                + "</html>\n"
                + "\"\"\")\n"
                + "\t}\n"
                + "}\n"
                , writer.toString())
        var code = writer.toString()
        print(code)

        //org.jetbrains.kotlin.cli.jvm.K2JVMCompiler.main()
    }
}