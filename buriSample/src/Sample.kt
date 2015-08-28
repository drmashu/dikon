import com.github.drmashu.buri.Buri
import com.github.drmashu.dikon.Factory
import com.github.drmashu.dikon.Holder
import com.github.drmashu.dikon.Injection
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