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
        compiler.precompile(reader, writer, "test")
        assertEquals("/** Generate source code by Buri Template PreCompiler at ${Date()}*/\n"
                + "class test : Renderer {\n"
                + "\tfun override render() : String {\n"
                + "\t\tvar ___buffer = StringBuffer()\n"
                + "/* 1 */___buffer.append(\"\"\"\n        \"\"\")\n"
                + "\t\treturn ___buffer.toString()\n"
                + "\t}\n"
                + "}\n"
                , writer.toString())
        print(writer.toString())
    }
    test fun testPreCompile() {
        val compiler = PreCompiler()
        val reader = StringReader("""@(list: List<String>)
<!html>
<!-- -->
<html>
<head></head>
<body>
drmashu@@gmail.com
<br>
<ol>
@for(idx in 0..10) {
    @if(idx % 3 == 0) {
        <li> san!
    } else if(idx % 2 == 0) {
        <li> even
    } else {
        <li> odd
    }
}
@//comment
@for(text in list)
{
    <li>@{text}
}
</ol>
<input name="aa" value="@{value}"/>
</body>
</html>
""")
        val writer = StringWriter(1024)
        compiler.precompile(reader, writer, "test")
        assertEquals("/** Generate source code by Buri Template PreCompiler at ${Date()}*/\n"
                + "class test : Renderer {\n"
                + "\tfun override render(list: List<String>) : String {\n"
                + "\t\tvar ___buffer = StringBuffer()\n"
                + "\t\treturn ___buffer.toString()\n"
                + "\t}\n"
                + "}\n"
                , writer.toString())
        var code = writer.toString();
        print(code)

    }
}