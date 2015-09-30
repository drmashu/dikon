/** Generate source code by Buri Template PreCompiler at Wed Sep 23 22:22:32 JST 2015 */
package test
import java.util.*
import java.io.Writer
import javax.servlet.http.*
import io.github.drmashu.buri.*
class template(request: HttpServletRequest, response: HttpServletResponse, val list: List<String>) : HtmlAction(request, response) {
	public override fun get() {
/* 1 */writer.write("""<!DOCTYPE html>
<!-- -->
<html>
<head></head>
<body>
drmashu""")
/* 6 */writer.write("@")
/* 6 */writer.write("""gmail.com
<br>
<ol>
""")
/* 9 */for(idx in 0..10) {
/* 10 */writer.write("""
    """)
/* 10 */if(idx % 3 == 0) {
/* 11 */writer.write("""
        <li> san!
    """)
/* 12 */} else if (idx % 2 == 0){
/* 13 */writer.write("""
        <li> even
    """)
/* 14 */} else {
/* 15 */writer.write("""
        <li> odd
    """)
/* 16 */}
/* 17 */writer.write("""
""")
/* 17 */}
/* 18 */writer.write("""
""")
/* 19 */writer.write("""""")
/* 19 */for(text in list) {
/* 20 */writer.write("""
    <li>""")
/* 20 */writer.write(encode("${text}"))
/* 21 */writer.write("""
""")
/* 21 */}
/* 22 */writer.write("""
</ol>
<input name="aa" value="""")
/* 23 */writer.write(encode("${this.toString()}"))
/* 23 */writer.write(""""/>
</body>
</html>
""")
	}
}
