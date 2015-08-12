package com.github.drmashu.dikon

import com.github.drmashu.dikon.factory.*

/**
 * Created by tnagasaw on 2015/08/07.
 */
fun main(args: Array<String>) {
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
    if (comp is TestComponent) {
        println( comp.A + comp.Test2?.B + comp.Test2?.A);
    }
}

data class TestComponent {
    @inject("B") var A : String? = null
    @inject var Test2 : TestComponent2? = null
}
data class TestComponent2(@inject("A") val B: String, val A: String = "X")
