package com.tomislav0.roomer.forms

import androidx.compose.runtime.mutableStateOf
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import ch.benlu.composeform.FormField
import ch.benlu.composeform.validators.EmailValidator
import ch.benlu.composeform.validators.MinLengthValidator

class LoginForm : Form() {
    override fun self(): Form {
        return this
    }

    @FormField
    val email = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableListOf(
            EmailValidator()
        )
    )

    @FormField
    val password = FieldState(
        state = mutableStateOf<String?>(null),
        validators = mutableListOf(
            MinLengthValidator(8, "The passwd must be at least 8 chars.")
        )
    )
}