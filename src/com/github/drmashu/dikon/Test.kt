package com.github.drmashu.dikon

/**
 * Created by tnagasaw on 2015/08/07.
 */
fun main(args: Array<String>) {
    val dikon = Dikon(hashMapOf(
            Pair("Test", SingletonFactory(kClass = TestComponent::class)),
            Pair("A", "A"),
            Pair("B", object: ObjectFactory<String> { override fun create():String? { return "B" }}),
            Pair("C", object: ObjectFactory<String> { override fun create():String? { return "B" } })
    ))
    val comp = dikon.get("Test")
    if (comp is TestComponent) {
        println( comp.A + comp.C?.B);
    }
}

data class TestComponent {
    var A : String? = null
    var C : TestComponent2? = null
}
data class TestComponent2(val A: String, val B: String)