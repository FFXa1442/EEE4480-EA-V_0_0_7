package com.sample.application.ea.res

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes

/**
 * A container for managing a TypedArray with automatic resource recycling.
 *
 * @param context The context to obtain the TypedArray from.
 * @param set The AttributeSet to obtain the attributes from, may be null.
 * @param attrs The styleable attributes to retrieve.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource.
 * @param defStyleRes A resource identifier of a style resource that supplies defaults values.
 */
class TypedArrayContainer(
    context: Context,
    set: AttributeSet?,
    @StyleableRes attrs: IntArray,
    @AttrRes defStyleAttr: Int,
    @StyleRes defStyleRes: Int
) : AutoCloseable {

    // Obtains the styled attributes set
    private val a: TypedArray =
        context.obtainStyledAttributes(
            set,
            attrs,
            defStyleAttr,
            defStyleRes
        )

    /**
     * Uses the TypedArray within a block and ensures it is recycled after use.
     *
     * @param block The block of code to execute with the TypedArray.
     */
    fun use(block: (TypedArray) -> Unit) {
        var exception: Throwable? = null
        try {
            // Executes the block with the TypedArray
            block(a)
        } catch (e: Throwable) {
            // Catches any exception thrown in the block
            exception = e
            throw e
        } finally {
            // Ensures the TypedArray is recycled and handles any exceptions
            closeFinally(exception)
        }
    }

    /**
     * Closes the TypedArray and manages exceptions.
     *
     * @param cause The exception cause to handle during closure.
     */
    private fun closeFinally(cause: Throwable?) = when {
        cause == null -> close() // Close normally if no exception occurred
        else ->
            try {
                close()
            } catch (closeException: Throwable) {
                // Add any exceptions during close to the original cause
                cause.addSuppressed(closeException)
            }
    }

    /**
     * Recycles the TypedArray to release its resources.
     */
    override fun close() {
        a.recycle() // Recycle the TypedArray
    }


}