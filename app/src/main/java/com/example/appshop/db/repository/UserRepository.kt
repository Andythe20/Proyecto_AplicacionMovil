package com.example.appshop.db.repository

import com.example.appshop.db.dao.UserDao
import com.example.appshop.db.entities.User

/**
 * Clase Repositorio para la entidad [User].
 *
 * El patrón de Repositorio es una capa de abstracción entre las fuentes de datos y el resto de la aplicación (en este caso, el ViewModel).
 * Su responsabilidad principal es gestionar las operaciones de datos y proporcionar un API limpia para que el ViewModel las consuma.
 *
 * @param userDao El objeto de acceso a datos (DAO) para la entidad User. Se inyecta a través del constructor para que el Repositorio pueda comunicarse con la base de datos.
 */
class UserRepository(private val userDao: UserDao) {
    /**
     * Inserta un nuevo usuario en la base de datos a través del DAO.
     * Esta función es una 'suspend function' (corrutina) porque la operación del DAO
     * debe ejecutarse en un hilo secundario para no bloquear la interfaz de usuario.
     *
     * @param user El objeto [User] a insertar.
     */
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    /**
     * Busca un usuario por su email.
     * Delega la llamada directamente al método correspondiente en el DAO.
     *
     * @param email El email del usuario a buscar.
     * @return El objeto [User] si se encuentra, o `null` si no existe un usuario con ese email.
     */
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    /**
     * Valida las credenciales de un usuario para el inicio de sesión.
     * Simplemente pasa los datos al método de login del DAO.
     *
     * @param email El email proporcionado por el usuario.
     * @param password La contraseña proporcionada por el usuario.
     * @return El objeto [User] si las credenciales son correctas, o `null` en caso contrario.
     */
    suspend fun login(
        email: String,
        password: String,
    ): User? {
        return userDao.login(email, password)
    }

    /**
     * Actualiza el perfil de un usuario existente.
     *
     * @param email El email del usuario a actualizar (identificador único).
     * @param name El nuevo nombre del usuario.
     * @param lastName El nuevo apellido del usuario (opcional).
     * @param address La nueva dirección del usuario (opcional).
     * @param imageProfileUri La nueva URI de la imagen de perfil (opcional).
     * @param birthdate La nueva fecha de nacimiento (opcional).
     */
    suspend fun updateUserProfile(
        email: String, // Usamos el email como identificador único del usuario
        name: String,
        lastName: String?,
        address: String?,
        imageProfileUri: String?,
        birthdate: String?,
    ){
        userDao.updateUserProfile(email, name, lastName, address, imageProfileUri, birthdate)
    }
}
