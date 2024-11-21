package com.sample.application.ea.utilities

import androidx.core.util.Consumer
import androidx.core.util.Function
import androidx.core.util.Supplier

/**
 * Utility object for safely handling nullable objects.
 */
object TryUtil {

    /**
     * Executes an action if the object is not null.
     *
     * @param obj The object to check.
     * @param objNonNull The action to perform if the object is not null.
     */
    @JvmStatic
    fun <T> tryNonNull(
        obj: T?,
        objNonNull: Consumer<T>
    ) = when (obj) {
        null -> {}
        else -> objNonNull.run { accept(obj) }
    }

    /**
     * Executes one action if the object is not null, otherwise another action.
     *
     * @param obj The object to check.
     * @param objNonNull The function to apply if the object is not null.
     * @param objNull The supplier to get the result if the object is null.
     * @return The result of the applied function or supplier.
     */
    @JvmStatic
    fun <R, T> tryNonNullElse(
        obj: T?,
        objNonNull: Function<T, R>,
        objNull: Supplier<R>
    ): R = when (obj) {
        null -> objNull.run { get() }
        else -> objNonNull.run { apply(obj) }
    }
}

