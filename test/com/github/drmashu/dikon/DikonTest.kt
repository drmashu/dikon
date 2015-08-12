package com.github.drmashu.dikon

import org.junit.Test as test
import com.github.drmashu.dikon.factory.Injection
import com.github.drmashu.dikon.factory.ObjectFactory
import com.github.drmashu.dikon.factory.Singleton
import kotlin.test.assertEquals

/**
 * Created by tnagasaw on 2015/08/07.
 */
class DikonTest() {
    test fun testDikon() {
        val dikon = Dikon(hashMapOf(
                Pair("Test", Singleton(TestComponent::class)),
                Pair("A", "A"),
                Pair("B", Singleton(object : ObjectFactory<String> {
                    override fun get(dikon: Dikon): String? {
                        return "B"
                    }
                })),
                Pair("Test2", Singleton(Injection(TestComponent2::class)))
        ))
        val comp = dikon.get("Test")
        assert(comp is TestComponent)
        if (comp is TestComponent) {
            assertEquals("B", comp.A)
            val comp2 = dikon.get("Test")
            assertEquals(comp, comp2)
            assertEquals("A", comp.Test2?.B)
            assertEquals(comp.Test2, dikon.get("Test2"))
        }
    }
}

data class TestComponent {
    @inject("B") var A : String? = null
    @inject var Test2 : TestComponent2? = null
}
data class TestComponent2(@inject("A") val B: String, val A: String = "X")
