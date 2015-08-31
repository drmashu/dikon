package com.github.drmashu.jkon

import org.junit.Test as test
import org.junit.Before as before
import org.junit.After as after

import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Created by tnagasaw on 2015/08/31.
 */
public class KtoJTest {
    test fun testConvert() {
        val obj = KtoJ().convert("{ \"key1\" : \"val1\", \"key2\" : \"val2\", \"key3\" : { \"ckey1\" : \"cval1\", \"ckey2\" : [ \"cval2-1\", \"cval2-2\" ] } }")

    }
}