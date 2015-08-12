package com.github.drmashu.dikon
import com.github.drmashu.dikon.factory.ObjectFactory
import java.lang.reflect.Method
import kotlin.reflect.*
import kotlin.reflect.jvm.*

/**
 * DI KONtainer.
 *
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 * @param objectMap 生成したいオブジェクトに名前を付けたMap
 */
public class Dikon(val objectMap: Map<String, Any>) {

    /**
     * オブジェクトの取得
     * @param name
     */
    public fun get(name:String) : Any? {
        val obj = objectMap[name]

        return injectProperties(
            if (obj == null) {
                null
            } else if (obj is ObjectFactory<*>) {
                obj.get(this)
            } else if (obj is KClass<*>) {
                obj.create()
            } else {
                obj
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
                members@ for(member in kClass.members) {
                    if (member is KMutableProperty1<*,*>) {
                        for (annotation in member.annotations) {
                            // 注入元指定のアノテーションがある場合は、その名称で注入する
                            if (annotation is inject) {
                                var name = member.name
                                if (!annotation.name.isEmpty()) {
                                    name = annotation.name
                                }
                                // set可能なプロパティを対象にする
                                callSetter(obj, member.javaSetter, get(name))
                                break
                            }
                        }
                    } else if (member is KFunction && member.name.startsWith("set")) {
                        for (annotation in member.annotations) {
                            // 注入元指定のアノテーションがある場合は、その名称で注入する
                            if (annotation is inject) {
                                var name = member.name.substring(3)
                                if (!annotation.name.isEmpty()) {
                                    name = annotation.name
                                }
                                // setで始まるメソッドを対象にする
                                callSetter(obj, member.javaMethod, get(name))
                                break
                            }
                        }
                    }
                }
            } catch (e:KotlinReflectionInternalError) {
                // Kotlinでのリフレクションが失敗する場合
                val jClass = obj.javaClass
                for(method in jClass.methods) {
                    val name = method.name
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
     * @param obj 対象のオブジェクト
     * @param setter セッターメソッド
     * @param value セットする値
     */
    protected fun callSetter(obj: Any?, setter: Method?, value: Any?) {
        if (setter != null && value != null) {
            val paramTypes = setter.parameterTypes
            if (paramTypes.size() == 1 && paramTypes[0].isAssignableFrom(value.javaClass)) {
                setter.invoke(obj, value)
            }
        }
    }
}

/**
 * 注入アノテーション
 * @param name
 */
annotation class inject(val name: String = "")

/**
 * デフォルトコンストラクタを探して呼び出す
 */
public fun <T> KClass<T>.create(): T? {
    var instance :T? = null
    for (it in this.constructors) {
        // デフォルトコンストラクタを探して呼び出す
        val params = it.parameters;
        if (params.size() == 0) {
            instance = it.call()
            break
        }
    }
    return instance
}
