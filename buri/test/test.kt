import com.github.drmashu.buri.Buri
import com.github.drmashu.dikon.Factory
import com.github.drmashu.dikon.Holder

/**
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 */
fun main(vararg args:String) {
    Buri(mapOf(
            Pair("/", Holder("index.html"))
    )).start(8080)
}