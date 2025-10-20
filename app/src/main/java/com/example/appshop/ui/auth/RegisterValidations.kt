package com.example.appshop.ui.auth

import com.example.appshop.utils.validateInputText


fun formatoCorreo(correo:String):Boolean{
    val regex = Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]+$")
    return regex.matches(correo.lowercase())
}

fun validarContrasena(contrasena: String, confirmContrasena: String): Boolean {
    if (contrasena != confirmContrasena){
        return false
    }
    else{
        return true
    }
}

fun validacionNombre(nombre: String): String? {
    if (nombre.isBlank()) {
        return "El nombre no puede estar vacío"
    } else if(nombre.length < 3){
        return validateInputText("Nombre usuario", nombre)
    } else {
        return null
    }
}

fun validacionEmail(email: String): String?{
    if (email.isBlank()) {
        return "El correo no puede estar vacío"
    } else if (!formatoCorreo(email)) {
        return "El formato del correo no es válido"
    } else{
        return null
    }
}

fun validacionContrasenna(contrasenna: String): String?{
    if (contrasenna.isBlank()) {
        return "La contraseña no puede estar vacía"
    } else if (!largoContrasena(contrasenna)) {
        return "La contraseña debe tener al menos 6 caracteres"
    } else{
        return null
    }
}


fun validarCampos(nombre: String, contrasena: String, validarContrasena: String, correo: String): Boolean{
    if (nombre.isBlank() || contrasena.isBlank() || validarContrasena.isBlank() || correo.isBlank()){
        return false
    }
    else{
        return true
    }
}

fun largoContrasena(contrasena: String): Boolean{
    if (contrasena.length < 6 || contrasena.isBlank()){
        return false
    } else{
        return true
    }
}


