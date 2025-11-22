package com.example.appshop.utils

import androidx.core.text.HtmlCompat

// Función para convertir un string HTML a texto plano
fun String.htmlToString(): String {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}