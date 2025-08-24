package com.bhuvanesh.task.utils

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrency(): String =
    NumberFormat.getNumberInstance(Locale("en", "IN")).format(this)