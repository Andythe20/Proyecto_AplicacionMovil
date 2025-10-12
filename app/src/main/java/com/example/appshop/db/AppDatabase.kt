package com.example.appshop.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appshop.db.dao.UserDao
import com.example.appshop.db.entities.User

/**
 * @Database (Base de Datos)
 * Anotación principal para definir la base de datos de Room.
 *
 * @param entities Un array de todas las clases de entidad que pertenecen a esta base de datos.
 *                 Cada clase listada aquí se convertirá en una tabla en la base de datos.
 * @param version El número de versión de la base de datos. Si cambias el esquema de la base de datos
 *                (por ejemplo, añades una columna a una tabla), DEBES incrementar este número.
 *                Esto es crucial para manejar las migraciones de la base de datos.
 * @param exportSchema Por defecto es `true`. Si se establece en `true`, Room exporta el esquema de la base de datos
 *                     a un archivo JSON en la carpeta del proyecto. Es útil para control de versiones del esquema.
 *                     Para proyectos más simples, se puede poner en `false`.
 */
@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Define un método abstracto que devuelve una instancia del DAO.
     * Room usará esta función para proporcionar una implementación concreta de `UserDao`
     * que podemos usar para interactuar con la tabla 'users'.
     * Debes declarar un método abstracto para cada DAO que tenga la base de datos.
     */
    abstract fun userDao(): UserDao

    /**
     * `companion object` es similar a los miembros estáticos en Java.
     * Lo usamos aquí para implementar el patrón Singleton, que asegura que solo se cree
     * una instancia de `AppDatabase` en toda la aplicación. Esto es muy eficiente
     * y previene problemas de concurrencia.
     */
    companion object {
        /**
         * @Volatile
         * La anotación `@Volatile` asegura que el valor de la variable `INSTANCE`
         * sea siempre el más actualizado y visible para todos los hilos (threads) de ejecución.
         * Es importante para garantizar que la instancia de la base de datos sea la misma en todas partes.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Función pública para obtener la instancia Singleton de la base de datos.
         * Este es el único punto de entrada para obtener una referencia a la base de datos.
         *
         * @param context El contexto de la aplicación, necesario para que Room pueda construir la base de datos.
         * @return La única instancia de [AppDatabase].
         */
        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia ya existe, la devuelve.
            // Si es nula, entra en un bloque sincronizado para crearla de forma segura.
            return INSTANCE ?: synchronized(this) {
                // `synchronized(this)` asegura que solo un hilo a la vez pueda ejecutar este bloque de código,
                // previniendo que se creen dos instancias de la base de datos al mismo tiempo si dos hilos
                // intentan acceder a ella simultáneamente.

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db" // El nombre del archivo de la base de datos en el dispositivo.
                )
                    // .fallbackToDestructiveMigration() // Opcional: Para migraciones. Si incrementas la versión,
                    // destruirá la base de datos antigua y creará una nueva.
                    // ¡CUIDADO! Se pierden todos los datos.
                    .build()

                // Asigna la nueva instancia creada y la devuelve.
                INSTANCE = instance
                instance
            }
        }
    }
}
