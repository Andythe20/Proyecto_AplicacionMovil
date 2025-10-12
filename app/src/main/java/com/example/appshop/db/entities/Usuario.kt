package com.example.appshop.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Entity (Entidad)
 * Esta anotación marca a esta `data class` como una tabla en la base de datos de Room.
 * Es la estructura o el "molde" de los datos que se guardarán.
 *
 * @param tableName = "users" define el nombre exacto de la tabla en la base de datos SQLite.
 * Si no se especifica, Room usaría el nombre de la clase ("User") como nombre de la tabla por defecto.
 */
@Entity(tableName = "users")
data class User(
    /**
     * @PrimaryKey (Clave Primaria)
     * Esta anotación identifica a la propiedad `id` como la clave primaria de la tabla.
     * Una clave primaria es un identificador único para cada fila (registro) en la tabla. No puede haber dos usuarios con el mismo `id`.
     *
     * @param autoGenerate = true le indica a Room que genere automáticamente un valor numérico único para el `id`
     * cada vez que se inserta un nuevo usuario. Esto es muy útil ya que no tenemos que preocuparnos
     * por asignar manualmente un `id` único. El valor inicial es 0, pero Room lo reemplazará al insertar.
     */
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
)
