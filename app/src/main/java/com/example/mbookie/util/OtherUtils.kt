package com.example.mbookie.util

import android.content.Context
import android.util.Patterns
import android.widget.Toast

fun CharSequence?.isValidEmail() =
    Patterns.EMAIL_ADDRESS.matcher(this.toString()).matches()

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}