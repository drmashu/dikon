package com.github.drmashu.dikon

import kotlin.reflect.KClass

/**
 * オブジェクトの生成方法を定義するためのインターフェイス
 * @author NAGASAWA Takahiro
 */
public interface ComponentFactory {
    fun create() : Any?
}

/**
 * シングルトンを実装するファクトリ
 * @author NAGASAWA Takahiro
 */
public open class SingletonFactory(val kClass: KClass<*>) : ComponentFactory {
    var instance :Any? = null
    public override fun create() : Any?
    {
        // インスタンスが存在しない場合だけ、インスタンスを作成する
        if (instance == null) {
            constructors@ for (it in kClass.constructors) {
                // デフォルトコンストラクタを探して呼び出す
                if (it.parameters.size() == 0) {
                    instance = it.call()
                    break@constructors
                }
            }
        }
        return instance
    }
}