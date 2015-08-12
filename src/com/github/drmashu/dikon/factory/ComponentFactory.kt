package com.github.drmashu.dikon.factory

import com.github.drmashu.dikon.Dikon
import com.github.drmashu.dikon.create
import com.github.drmashu.dikon.inject
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * オブジェクトの生成方法を定義するためのインターフェイス
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 */
public interface ObjectFactory<T> {
    /**
     * インスタンスの取得
     * @return インスタンス
     */
    fun get(dikon: Dikon) : T?
}

/**
 * シングルトンを実装するファクトリ
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 */
public open class Singleton<T>(val factory: ObjectFactory<T>?, val kClass: KClass<T>?) : ObjectFactory<T> {

    /**
     * コンストラクタ,
     * @param factory 対象のインスタンスを作成するファクトリ
     */
    public constructor(factory: ObjectFactory<T>) : this(factory, null)

    /**
     * コンストラクタ,
     * クラスを指定する場合は、デフォルトコンストラクタが存在すること。
     * @param kClass 対象のインスタンスのクラス
     */
    public constructor(kClass: KClass<T>) : this(null, kClass)

    /**
     * インスタンス
     */
    private var instance :T? = null

    /**
     * インスタンスの取得
     * @return インスタンス
     */
    public override fun get(dikon: Dikon) : T? {
        // インスタンスが存在しない場合だけ、インスタンスを作成する
        if (instance == null) {
            if (factory != null) {
                instance = factory.get(dikon)
            } else if (kClass != null) {
                instance = kClass.create()
            }
        }
        return instance
    }
}

/**
 * コンストラクターインジェクションを行うファクトリ.
 * コンストラクターインジェクションはプライマリコンストラクタを対象に行われる。
 * インジェクションさせない項目はnull可とすること。
 * デフォルト値を設定してもnullを設定するため注意。
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 */
public open class Injection<T>(val kClass: KClass<T>) : ObjectFactory<T> {

    /**
     * インスタンスの取得
     * @return インスタンス
     */
    public override fun get(dikon: Dikon): T? {
        val constructor = kClass.primaryConstructor;
        if (constructor != null) {
            val params = constructor.parameters
            var paramArray = Array<Any?>(params.size(), {null});
            for (idx in params.indices) {
                val param = params[idx]
                var name = param.name
                for (anno in param.annotations) {
                    if (anno is inject) {
                        name = anno.name
                        break
                    }
                }
                if (name != null) {
                    paramArray[idx] = dikon.get(name)
                }
            }
            return constructor.call(*paramArray)
        }
        return null
    }
}
