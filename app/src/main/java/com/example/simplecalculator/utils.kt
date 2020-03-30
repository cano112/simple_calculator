package com.example.simplecalculator

import android.text.Editable
import android.widget.EditText
import java.math.BigDecimal

fun EditText.bigDecimal() = BigDecimal(text.toString())

fun BigDecimal.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this.toString())
