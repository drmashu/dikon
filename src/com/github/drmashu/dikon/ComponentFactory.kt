package com.github.drmashu.dikon

import kotlin.reflect.KClass

/**
 * オブジェクトの生成方法を定義するためのインターフェイス
 * @author NAGASAWA Takahiro
 */
public interface ObjectFactory<T> {
    fun create() : T?
}

/**
 * シングルトンを実装するファクトリ
 * @author NAGASAWA Takahiro
 */
public open class SingletonFactory<T>(val kClass: KClass<T>) : ObjectFactory<T> {
    var instance :T? = null
    public override fun create() : T?
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
