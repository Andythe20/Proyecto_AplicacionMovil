package com.example.appshop.utils

/**
 * Valida un campo de texto genérico.
 * @param inputName Nombre del campo (para mensajes de error).
 * @param value Valor del campo a validar.
 * @param minLength Longitud mínima requerida (por defecto es 3).
 * @return Mensaje de error si la validación falla, o null si es válido.
 */
fun validateInputText(inputName: String, value: String, minLength: Int = 3): String? {
    return when {
        value.isBlank() -> "$inputName es obligatorio"
        value.trim().length < minLength -> "$inputName debe tener al menos $minLength caracteres"
        else -> null
    }
}

/**
 * Valida un campo numérico entero.
 * @param inputName Nombre del campo (para mensajes de error).
 * @param value Valor del campo a validar.
 * @param minValue Valor mínimo permitido (por defecto es 0).
 * @return Mensaje de error si la validación falla, o null si es válido.
 */
fun validateIntField(inputName: String, value: String, minValue: Int = 0): String? {
    if(value.isBlank()) return "$inputName es obligatorio"
    val number = value.toIntOrNull() ?: return  "$inputName debe ser un número entero"
    return if (number < minValue) "$inputName debe ser mayor o igual a $minValue" else null
}