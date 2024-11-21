package com.sample.application.ea.functions

import android.widget.EditText

object PasswordValidation {

    private const val MIN_LENGTH = "8"

    private const val MAX_LENGTH: String = "20"

    private const val ONE_DIGIT: String = "(?=.*[0-9])"
    private const val LOWER_CASE: String = "(?=.*[a-z])"
    private const val UPPER_CASE: String = "(?=.*[A-Z])"

    private const val NO_SPACE: String = "(?=\\S+$)"

    /**
     * Validates the password against a pattern with optional special character requirement.
     *
     * @param password The password string to validate.
     * @param specialCase Indicates if a special character is required.
     * @return True if the password matches the pattern, false otherwise.
     */
    @JvmStatic
    fun patternMatches(password: String, specialCase: Boolean): Boolean {
        // Include special character requirement if specialCase is true
        val specialChar = if (specialCase) "(?=.*[@#$%^&+=])" else ""
        val minMaxChar = ".{$MIN_LENGTH,$MAX_LENGTH}"
        val pattern = ONE_DIGIT + LOWER_CASE + UPPER_CASE + specialChar + NO_SPACE + minMaxChar
        return password.matches(pattern.toRegex())
    }

    /**
     * Validates the password against a basic pattern without special character requirement.
     *
     * @param password The password string to validate.
     * @return True if the password matches the pattern, false otherwise.
     */
    @JvmStatic
    fun patternMatches(password: String): Boolean {
        return patternMatches(password, false)
    }

    /**
     * Validates the password from the given EditText against a pattern with optional special character requirement.
     *
     * @param editText The EditText containing the password to validate.
     * @param specialCase Indicates if a special character is required.
     * @return True if the password matches the pattern, false otherwise.
     */
    @JvmStatic
    fun patternMatches(editText: EditText, specialCase: Boolean): Boolean {
        return patternMatches(editText.text.toString(), specialCase)
    }

    /**
     * Validates the password from the given EditText against a basic pattern without special character requirement.
     *
     * @param editText The EditText containing the password to validate.
     * @return True if the password matches the pattern, false otherwise.
     */
    @JvmStatic
    fun patternMatches(editText: EditText): Boolean {
        return patternMatches(editText.text.toString())
    }


}