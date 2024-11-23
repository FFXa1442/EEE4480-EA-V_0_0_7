package com.sample.application.ea.functions

import android.widget.EditText

object PasswordValidation {

    private const val MIN_LENGTH = "8"

    private const val MAX_LENGTH: String = "20"

    private const val ONE_DIGIT: String = "(?=.*[0-9])"
    private const val LOWER_CASE: String = "(?=.*[a-z])"
    private const val UPPER_CASE: String = "(?=.*[A-Z])"

    private const val NO_SPACE: String = "(?=\\S+$)"

    @JvmStatic
    fun patternMatches(password: String, specialCase: Boolean): Boolean {
        val specialChar = if (specialCase) "(?=.*[@#$%^&+=])" else ""
        val minMaxChar = ".{$MIN_LENGTH,$MAX_LENGTH}"
        val pattern = ONE_DIGIT + LOWER_CASE + UPPER_CASE + specialChar + NO_SPACE + minMaxChar
        return password.matches(pattern.toRegex())
    }

    @JvmStatic
    fun patternMatches(
        password: String,
    ) = patternMatches(
        password,
        specialCase = false
    )

    @JvmStatic
    fun patternMatches(
        editText: EditText,
        specialCase: Boolean,
    ) = patternMatches(
        password = editText.text.toString(),
        specialCase = specialCase
    )

    @JvmStatic
    fun patternMatches(
        editText: EditText,
    ) = patternMatches(
        password = editText.text.toString()
    )


}