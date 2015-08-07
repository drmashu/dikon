package com.github.drmashu.dikon
import java.lang.reflect.Method
import kotlin.reflect.*
import kotlin.reflect.jvm.*

/**
 * DI KONtainer
 * @author NAGASAWA Takahiro
 */
public class Dikon(val factories:Map<String, Any>) {

    /**
     * オブジェクトの取得
     * @param name
     */
    public fun get(name:String) : Any? {
        val factory = factories[name]

        return injectProperties(
            if (factory == null) {
                null
            } else if (factory is ComponentFactory) {
                factory.create()
            } else {
                factory
            }
        )
    }

    /**
     * プロパティへの依存性注入
     * @param obj 対象のオブジェクト
     */
    protected fun injectProperties(obj: Any?): Any? {
        if (obj != null) {
            val kClass = obj.javaClass.kotlin
            try {
                for(member in kClass.members) {
                    if (member is KMutableProperty1<*,*>) {
                        // set可能なプロパティを対象にする
                        callSetter(obj, member.javaSetter,  get(member.name))
                    } else if (member is KFunction && member.name.startsWith("set")) {
                        // setで始まるメソッドを対象にする
                        callSetter(obj, member.javaMethod, get(member.name.substring(3)))
                    }

                }
            } catch (e:KotlinReflectionInternalError) {
                // Kotlinでのリフレクションが失敗する場合
                val jClass = obj.javaClass
                for(method in jClass.getMethods()) {
                    val name = method.getName()
                    if (name.startsWith("set")) {
                        // setで始まるメソッドを対象にする
                        callSetter(obj, method, get(name.substring(3)))
                    }
                }
            }
        }
        return obj
    }

    /**
     * setterに指定可能な値が取得できていたら注入する
     */
    protected fun callSetter(obj: Any?, setter: Method?, value: Any?) {
        if (setter != null && value != null) {
            val paramTypes = setter.getParameterTypes()
            if (paramTypes.size() == 1 && paramTypes[0].isAssignableFrom(value.javaClass)) {
                setter.invoke(obj, value)
            }
        }
    }
}

