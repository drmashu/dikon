package com.github.drmashu.jkon

import jdk.nashorn.api.scripting.ScriptObjectMirror
import java.lang.reflect.Method
import java.util.*
import java.util.Set
import javax.script.ScriptEngineManager
import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaType
import kotlin.reflect.primaryConstructor

/**
 * Created by tnagasaw on 2015/08/31.
 */
public class KtoJ {
    public fun convert(target: String): Map<String, Any> {
        val obj = parse(target)
        return jsonToMap(obj)
    }

    private fun parse(target: String): ScriptObjectMirror {
        val manager = ScriptEngineManager();
        val engine = manager.getEngineByName("JavaScript");
        val obj = engine.eval("($target)");
        return obj as ScriptObjectMirror
    }

    public fun convert<T:Any>(target: String, kClass: KClass<T>): T {
        val obj = parse(target)

        return jsonToObject(obj, kClass)
    }

    private fun <T : Any> jsonToObject(obj: ScriptObjectMirror, kClass: KClass<T>): T {
        val construtor = kClass.primaryConstructor
        val params = construtor!!.parameters
        var paramArray = Array<Any?>(params.size(), { null })
        for (idx in params.indices) {
            val param = params[idx]
            val value = obj.get(param.name)
            if (value is ScriptObjectMirror) {
                paramArray[idx] = jsonToObject(value, if(param.type.javaType is Class<*>) {(param.type.javaType as Class<*>).kotlin }else{ param.type.javaType.javaClass.kotlin })
            } else {
                paramArray[idx] = value
            }
        }
        return construtor.call(*paramArray)
    }

    fun jsonToMap(obj: ScriptObjectMirror): Map<String, Any> {
        val keys = obj.keySet().toTypedArray()
        val map = HashMap<String, Any>()
        for (key in keys) {
            val value = obj.get(key)
            if (value is ScriptObjectMirror) {
                map.put(key.toString(), jsonToMap(value))
            } else {
                map.put(key.toString(), value!!)
            }
        }
        return map;
    }

}