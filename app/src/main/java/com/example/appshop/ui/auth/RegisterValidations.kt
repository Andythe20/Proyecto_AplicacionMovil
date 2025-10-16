package com.example.appshop.ui.auth


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


