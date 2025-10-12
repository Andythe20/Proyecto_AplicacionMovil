package com.example.appshop.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appshop.db.entities.User

/**
 * @Dao (Data Access Object)
 * Esta anotación le dice a Room que esta interfaz es un "Objeto de Acceso a Datos".
 * Es el componente responsable de definir los métodos para interactuar
 * con la base de datos (leer, escribir, actualizar, borrar).
 * Room generará automáticamente el código necesario para implementar estos métodos.
 */
@Dao
interface UserDao {

    /**
     * Inserta un nuevo usuario en la tabla 'users'.
     *
     * @param user El objeto [User] que se va a guardar en la base de datos.
     *
     * @Insert La anotación para operaciones de inserción.
     * @param onConflict = OnConflictStrategy.REPLACE le indica a Room qué hacer si
     * intentamos insertar un usuario con una clave primaria (`id` o `email` si fuera único) que ya existe.
     * En este caso, reemplazará al usuario antiguo con el nuevo.
     *
     * `suspend` Esta función es una corrutina, lo que permite que Room la ejecute en un hilo
     * de segundo plano para no bloquear la interfaz de usuario (UI).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    /**
     * Valida las credenciales del usuario durante el inicio de sesión.
     *
     * @param email El correo electrónico introducido por el usuario.
     * @param password La contraseña introducida por el usuario.
     * @return Un objeto [User] si se encuentra una coincidencia exacta, o `null` si las credenciales son incorrectas.
     *
     * @Query Permite ejecutar una consulta SQL personalizada. La consulta se verifica en tiempo de compilación,
     * lo que previene errores de SQL en tiempo de ejecución.
     * ":email" y ":password" son parámetros que se reemplazarán por los valores de la función.
     *
     * ¡ADVERTENCIA DE SEGURIDAD! Este método de login no es seguro porque compara la contraseña
     * en texto plano. En una aplicación real, se debería guardar un hash de la contraseña y
     * comparar el hash, no el texto plano.
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(
        email: String,
        password: String,
    ): User?

    /**
     * Busca y recupera un usuario específico por su dirección de correo electrónico.
     * Se usa principalmente para verificar si un email ya está registrado antes de crear una nueva cuenta.
     *
     * @param email El correo electrónico a buscar.
     * @return El objeto [User] correspondiente a ese email, o `null` si no se encuentra ninguno.
     */
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}

