package com.github.drmashu.jkon

import java.lang.reflect.Method
import java.util.*
import java.util.Set
import javax.script.ScriptEngineManager
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * Created by tnagasaw on 2015/08/31.
 */
public class KtoJ {
    public fun convert(target: String): Map<String, Any> {
        val manager = ScriptEngineManager();
        val engine = manager.getEngineByName("JavaScript");
        val obj = engine.eval("($target)");
        return jsonToMap(obj)
    }

    public fun convert<T:Any>(target: String, kClass: KClass<T>): T {
        val construtor = kClass.primaryConstructor
        val paramList = ArrayList<Any>()

        for (param in construtor!!.parameters) {
        }
        return construtor.call(*paramList.toArray())
    }

    fun jsonToMap(obj: Any): Map<String, Any> {
        val scriptObjectClass = Class.forName("jdk.nashorn.api.scripting.ScriptObjectMirror")
        val keys = (obj.javaClass.getMethod("keySet").invoke(obj) as Set<*>).toArray()
        // get メソッドを取得
        val method_get = obj.javaClass.getMethod("get", Class.forName("java.lang.Object"))
        val map = HashMap<String, Any>()
        for (key in keys) {
            val value = method_get.invoke(obj, key)
            if (scriptObjectClass.isInstance(value)) {
                map.put(key.toString(), jsonToMap(value))
            } else {
                map.put(key.toString(), value)
            }
        }
        return map;
    }

}