package com.sample.application.ea.functions

import android.widget.EditText
import java.util.regex.Pattern

object EmailValidation {

    private const val RFC5322_REGEX_PATTEN: String =
        "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"

    private val patten: Pattern by lazy {
        Pattern.compile(
            RFC5322_REGEX_PATTEN
        )
    }

    @JvmStatic
    fun patternMatches(
        email: String,
    ) = patten.matcher(
        email
    ).matches()

    @JvmStatic
    fun patternMatches(
        editText: EditText,
    ) = patternMatches(
        editText.getText().toString()
    )

}