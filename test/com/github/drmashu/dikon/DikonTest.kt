package com.github.drmashu.dikon

import org.junit.Test as test
import org.junit.Before as before
import org.junit.After as after

import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Created by tnagasaw on 2015/08/07.
 */
class DikonTest() {
    val dikon = Dikon(hashMapOf(
            Pair("Test", Singleton(TestComponent::class)),
            Pair("A", "A"),
            Pair("B", Singleton(object : Factory<String> {
                override fun get(dikon: Dikon): String? {
                    return "B"
                }
            })),
            Pair("Test2", Singleton(Injection(TestComponent2::class))),
            Pair("Test3", object : Factory<TestComponent3> {
                override fun get(dikon: Dikon): TestComponent3? {
                    val result = TestComponent3()
                    result.setA(dikon["A"] as String)
                    return result
                }
            })
    ))
    test fun testDikonGet() {
        val comp = dikon["Test"]
        assertNotNull(comp)
        assert(comp is TestComponent)
    }
    test fun testDikonInjection() {
        val comp = dikon["Test"]
        if (comp is TestComponent) {
            assertEquals("B", comp.A)
        }
    }
    test fun testDikonNotInjection() {
        val comp = dikon["Test"]
        if (comp is TestComponent) {
            assertNull(comp.B)
        }
    }
    test fun testSingleton() {
        val comp = dikon["Test"]
        if (comp is TestComponent) {
            val comp2 = dikon["Test"]
            assert(comp == comp2)
        }
    }
    test fun testInjection() {
        val comp = dikon["Test"]
        if (comp is TestComponent) {
            assertEquals("A", comp.Test2!!.A)
        }
    }
    test fun testInjectionWithAnnotation() {
        val comp = dikon.get("Test")
        if (comp is TestComponent) {
            assertEquals("A", comp.Test2!!.B)
        }
    }

    test fun testInjectionGet() {
        val comp = Injection(TestComponent2::class).get(dikon)
        if (comp is TestComponent2) {
            assertEquals("A", comp.A)
            assertEquals("A", comp.B)
        }
    }
    test fun testSingletonGet() {
        val comp = Singleton(TestComponent::class).get(dikon)
        assert(comp is TestComponent)
        if (comp is TestComponent) {
            assertNull(comp.A)
            assertNull(comp.B)
            assertNull(comp.Test2)
        }
    }
    test fun testSingletonInjectionGet() {
        val comp = Singleton(Injection(TestComponent2::class)).get(dikon)
        if (comp is TestComponent2) {
            assertEquals("A", comp.A)
            assertEquals("A", comp.B)
        }
    }
    test fun testDikonSetterInjection() {
        val comp = dikon["Test"]
        if (comp is TestComponent) {
            assertEquals("A", comp.C)
        }
    }
    test fun testDikonJavaClass() {
        val comp = dikon["Test3"]
        if (comp is TestComponent3) {
            assertEquals("A", comp.a)
        }
    }
}

data class TestComponent {
    @inject("B") var A : String? = null
    @inject var Test2 : TestComponent2? = null
    var B: String? = null
    var C: String? = null
    @inject("A") fun setCC(c:String) {
        C = c
    }
}
data class TestComponent2(@inject("A") val B: String, val A: String = "X")
