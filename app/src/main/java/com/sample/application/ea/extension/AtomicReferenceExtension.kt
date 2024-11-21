package com.sample.application.ea.extension

import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty


operator fun <T> AtomicReference<T>.getValue(
    o: T?,
    property: KProperty<*>,
): T = this.get()

operator fun <T> AtomicReference<T>.setValue(
    o: T?,
    property: KProperty<*>,
    o1: T?,
) = this.set(o1)