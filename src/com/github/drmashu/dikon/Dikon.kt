package com.github.drmashu.dikon

/**
 * Created by NAGASAWA Takahiro on 2015/08/06.
 */
public class Dikon(val components:Map<String, ComponetFactory>) {

    fun get(name:String) : Object? {
        var factory = components[name]
        return if (factory == null) {
            null
        } else {
            factory.create()
        }
    }

}

interface ComponetFactory {
    fun create() : Object?
}
