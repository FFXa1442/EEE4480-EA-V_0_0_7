package com.sample.application.ea.extension

import android.view.View
import android.view.ViewGroup
import androidx.core.util.Consumer

/**
 * ViewExtension is an object that provides extension functions for views.
 */
object ViewExtension {

    /**
     * Retrieves the layout parameters of a given view and casts them to the specified type.
     *
     * @param T The type of the view.
     * @param R The type of the layout parameters.
     * @param view The view whose layout parameters are to be retrieved.
     * @return The layout parameters of the view cast to the specified type.
     * @throws ClassCastException If the layout parameters cannot be cast to the specified type.
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : View, R : ViewGroup.LayoutParams> getLayoutParams(
        view: T
    ): R {
        return view.layoutParams as R
    }


}
