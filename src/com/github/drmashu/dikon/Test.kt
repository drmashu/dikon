package com.github.drmashu.dikon

/**
 * Created by tnagasaw on 2015/08/07.
 */
fun main(args: Array<String>) {
    val dikon = Dikon(hashMapOf(
            Pair("Test", SingletonFactory(kClass = TestComponent::class)),
            Pair("A", object: ComponentFactory { override fun create():Any? { return "A" }}),
            Pair("B", object: ComponentFactory { override fun create():Any? { return "B" }})
    ))
    val comp = dikon.get("Test")
    if (comp is TestComponent) {
        println( comp.A );
    }
}

class TestComponent {
    var A : String? = null
}