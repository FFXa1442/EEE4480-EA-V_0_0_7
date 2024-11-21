package com.sample.application.ea.extension

import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KProperty

operator fun AtomicBoolean.getValue(
    nothing: Nothing?,
    property: KProperty<*>,
) = this.get()

operator fun AtomicBoolean.setValue(
    nothing: Nothing?,
    property: KProperty<*>,
    b: Boolean,
) = this.set(b)