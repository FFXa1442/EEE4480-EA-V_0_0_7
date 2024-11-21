package com.sample.application.ea.functions

import android.widget.EditText
import java.util.regex.Pattern

object EmailValidation {

    // RFC 5322 regex pattern for validating email addresses
    private const val RFC5322_REGEX_PATTEN: String =
        "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"

    // Lazy initialization of the Pattern object
    private val patten: Pattern by lazy {
        Pattern.compile(
            RFC5322_REGEX_PATTEN
        )
    }

    /**
     * Checks if the given email string matches the RFC 5322 pattern.
     *
     * @param email The email string to validate.
     * @return True if the email matches the pattern, false otherwise.
     */
    @JvmStatic
    fun patternMatches(
        email: String
    ) = patten.matcher(email).matches()

    /**
     * Checks if the text from the given EditText matches the RFC 5322 pattern.
     *
     * @param editText The EditText containing the email to validate.
     * @return True if the email matches the pattern, false otherwise.
     */
    @JvmStatic
    fun patternMatches(
        editText: EditText
    ) = patternMatches(editText.getText().toString())

}