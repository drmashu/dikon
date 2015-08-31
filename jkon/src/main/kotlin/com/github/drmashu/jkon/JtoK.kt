package com.github.drmashu.jkon

import kotlin.reflect.memberProperties

/**
 * Created by tnagasaw on 2015/08/31.
 */
public class JtoK {
    fun convert(target: Any): String {
        val result = StringBuffer()
        return when (target) {
            is Map<*, *> -> convertMap(result, target)
            is Array<*> -> convertArray(result, target)
            else -> convert(result, target)
        }.toString()
    }

    private fun convert(result: StringBuffer, target: Any): StringBuffer {
        val kClass = target.javaClass.kotlin
        val properties = kClass.memberProperties
        result.append('{')
        for(property in properties) {
            val value = property.getter.call()
            if (value != null) {
                result.append('"').append(property.name).append("\":")
                appendValue(result, value).append(',')
            }
        }
        result.append('}')
        return result
    }

    private fun appendValue(result: StringBuffer, value: Any?): StringBuffer {
        when (value) {
            is String -> result.append('"').append(value).append('"')
            is Int, is Long, is Double, is Float -> result.append(value)
            is Boolean -> result.append(if(value) "true" else "false")
            is Map<*, *> -> convertMap(result, value)
            is Array<*> -> convertArray(result, value)
            is Any -> convert(result, value)
        }
        return result
    }

    private fun convertArray(result: StringBuffer, target: Array<*>): StringBuffer {
        result.append('[')
        for(item in target) {
            appendValue(result, item).append(',')
        }
        result.append(']')
        return result
    }

    private fun convertMap(result: StringBuffer, target: Map<*, *>): StringBuffer {
        result.append('{')
        for(elem in target.iterator()) {
            if (elem.value != null) {
                result.append("\"").append(elem.key).append("\":")
                appendValue(result, elem.value).append(',')
            }
        }
        result.append('}')
        return result
    }
}