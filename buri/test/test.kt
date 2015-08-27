import com.github.drmashu.buri.Buri
import com.github.drmashu.dikon.Holder
import com.github.drmashu.dikon.Injection

/**
 * Created by tnagasaw on 2015/08/27.
 */

fun main(vararg ags:String) {
    val buri = Buri(mapOf(
            Pair("/", Injection(test.template::class)),
            Pair("list", Holder(listOf("aa", "bb", "cc")))
    ))
    buri.start(8080)
}