package com.sample.application.ea.res

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes

class TypedArrayContainer(
    context: Context,
    set: AttributeSet?,
    @StyleableRes attrs: IntArray,
    @AttrRes defStyleAttr: Int,
    @StyleRes defStyleRes: Int
) : AutoCloseable {

    private val a: TypedArray =
        context.obtainStyledAttributes(
            set,
            attrs,
            defStyleAttr,
            defStyleRes
        )

    fun use(block: (TypedArray) -> Unit) {
        var exception: Throwable? = null
        try {
            block(a)
        } catch (e: Throwable) {
            exception = e
            throw e
        } finally {
            closeFinally(exception)
        }
    }

    private fun closeFinally(cause: Throwable?) = when {
        cause == null -> close()
        else ->
            try {
                close()
            } catch (closeException: Throwable) {
                cause.addSuppressed(closeException)
            }
    }

    override fun close() = a.recycle()


}