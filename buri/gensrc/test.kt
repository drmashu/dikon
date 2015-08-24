/** Generate source code by Buri Template PreCompiler at Sun Aug 23 18:49:12 JST 2015*/
import java.util.*
import java.io.Writer
import com.github.drmashu.buri.Renderer

class test(___writer___: Writer, val list: List<String>) : Renderer(___writer___) {
    public override fun render() {
        /* 1 */
        ___writer___.write("""<!html>
<!-- -->
<html>
<head></head>
<body>
drmashu""")
        /* 6 */
        ___writer___.write("@")
        /* 6 */
        ___writer___.write("""gmail.com
<br>
<ol>override
""")
        /* 9 */
        for (idx in 0..10) {
            /* 10 */
            ___writer___.write("""
    """)
            /* 10 */
            if (idx % 3 == 0) {
                /* 11 */
                ___writer___.write("""
        <li> san!
    """)
                /* 12 */
            } else if (idx % 2 == 0) {
                /* 13 */
                ___writer___.write("""
        <li> even
    """)
                /* 14 */
            } else {
                /* 15 */
                ___writer___.write("""
        <li> odd
    """)
                /* 16 */
            }
            /* 17 */
            ___writer___.write("""
""")
            /* 17 */
        }
        /* 18 */
        ___writer___.write("""
""")
        /* 19 */
        for (text in list) {
            /* 20 */
            ___writer___.write("""
    <li>""")
            /* 20 */
            ___writer___.write("${text}")
            /* 21 */
            ___writer___.write("""
""")
            /* 21 */
        }
        /* 22 */
        ___writer___.write("""
</ol>
<input name="aa" value="""")
        /* 23 */
        ___writer___.write("${this.toString()}")
        /* 23 */
        ___writer___.write(""""/>
</body>
</html>
""")
    }
}
