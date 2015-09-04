import io.github.drmashu.buri.Buri
import io.github.drmashu.dikon.Factory
import io.github.drmashu.dikon.Holder
import io.github.drmashu.dikon.Injection
import test.template

/**
 * Created by tnagasaw on 2015/08/28.
 */
public class Sample : Buri() {
    override val config: Map<String, Factory<*>>
        get() =mapOf(
                Pair("/", Injection(template::class)),
                Pair("list", Holder(listOf("aa", "bb", "cc")))
        )
}