package com.sample.application.ea.text

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

inline fun TextView.doBeforeTextChanged(
    crossinline action: (text: CharSequence?) -> Unit = {},
) = addTextChangedListener(beforeTextChanged = action)

inline fun TextView.doOnTextChanged(
    crossinline action: (text: CharSequence?) -> Unit = {},
) = addTextChangedListener(onTextChanged = action)

inline fun TextView.addTextChangedListener(
    crossinline beforeTextChanged:
        (text: CharSequence?) -> Unit = {},
    crossinline onTextChanged:
        (text: CharSequence?) -> Unit = {},
): TextWatcher {
    val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s)
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }
    addTextChangedListener(textWatcher)
    return textWatcher
}