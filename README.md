# AppShop - Aplicación Android con Jetpack Compose y Room

Este documento describe la arquitectura y las tecnologías clave utilizadas en la aplicación **AppShop**. El objetivo principal de este proyecto es demostrar la implementación de una base de datos local con SQLite a través de Room, siguiendo el patrón de diseño MVVM y las mejores prácticas de desarrollo Android.

## Tabla de Contenidos

1.  [Tecnologías Utilizadas](#tecnologías-utilizadas)
2.  [Arquitectura de la Aplicación (Patrón MVVM)](#arquitectura-de-la-aplicación-patrón-mvvm)
3.  [Capa de Datos: Room y SQLite](#capa-de-datos-room-y-sqlite)
    -   [Entity (Entidad)](#1-entity-entidad---userkt)
    -   [DAO (Data Access Object)](#2-dao-data-access-object---userdaokt)
    -   [Database (Base de Datos) y Patrón Singleton](#3-database-base-de-datos-y-patrón-singleton---appdatabasekt)
4.  [Capa de Lógica de Negocio](#capa-de-lógica-de-negocio)
    -   [Repository (Repositorio)](#1-repository-repositorio---userrepositorykt)
    -   [ViewModel (Vista-Modelo)](#2-viewmodel-vista-modelo---authviewmodelkt)
5.  [Capa de UI (Interfaz de Usuario)](#capa-de-ui-interfaz-de-usuario)
    -   [Jetpack Compose y State Hoisting](#1-jetpack-compose-y-state-hoisting)
    -   [Navegación (Navigation Compose)](#2-navegación-navigation-compose---appnavigationkt)
6.  [Cómo Probar la Base de Datos](#cómo-probar-la-base-de-datos)

---

## Tecnologías Utilizadas

-   **Lenguaje:** [Kotlin](https://kotlinlang.org/)
-   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
-   **Arquitectura:** [MVVM (Model-View-ViewModel)](https://developer.android.com/jetpack/guide)
-   **Base de Datos:** [Room](https://developer.android.com/training/data-storage/room) (abstracción sobre SQLite)
-   **Asincronía:** [Corrutinas de Kotlin](https://developer.android.com/kotlin/coroutines)
-   **Navegación:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
-   **Inyección de Dependencias (manual):** Se utiliza una `ViewModelFactory` para inyectar dependencias en el `ViewModel`.

---

## Arquitectura de la Aplicación (Patrón MVVM)

La aplicación sigue un patrón de diseño **Model-View-ViewModel (MVVM)**, que separa las responsabilidades en tres capas principales:

-   **Model (Capa de Datos):** Gestiona los datos de la aplicación. En nuestro caso, está compuesta por la base de datos Room (Entidades, DAO, Database) y el Repositorio. No tiene conocimiento de la UI.
-   **View (Capa de UI):** La interfaz de usuario (`Composables` como `SignupScreen`). Es "tonta", lo que significa que solo muestra los datos que le proporciona el ViewModel y le notifica sobre las interacciones del usuario (clics, etc.).
-   **ViewModel (Capa de Lógica de UI):** Actúa como un puente entre la UI y la capa de datos. Contiene la lógica de presentación, procesa las acciones del usuario y expone los datos que la UI debe mostrar.


![MVVM Diagram](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

*Diagrama oficial de la arquitectura recomendada por Google.*

---

## Capa de Datos: Room y SQLite

**Room** es una librería que crea una capa de abstracción sobre SQLite para permitir un acceso más robusto y sencillo a la base de datos, manteniendo al mismo tiempo todo el poder de SQLite.

### 1. Entity (Entidad) - `User.kt`

-   Una clase de datos (`data class`) anotada con `@Entity`.
-   Representa una tabla en la base de datos. Cada instancia de la clase es una fila en la tabla, y cada propiedad es una columna.
-   **Ejemplo:** La clase `User` define la tabla `users` con columnas como `id`, `name`, `email` y `password`.

### 2. DAO (Data Access Object) - `UserDao.kt`

-   Una interfaz anotada con `@Dao`.
-   Define los métodos para interactuar con la base de datos (consultas, inserciones, etc.) mediante anotaciones como `@Query`, `@Insert`.
-   Room genera automáticamente el código necesario en tiempo de compilación.
-   Todas las funciones son `suspend`, lo que significa que deben ser llamadas desde una corrutina para no bloquear el hilo principal.

### 3. Database (Base de Datos) y Patrón Singleton - `AppDatabase.kt`

-   Una clase abstracta que hereda de `RoomDatabase` y está anotada con `@Database`.
-   Actúa como el punto de acceso principal a la base de datos.
-   Define qué entidades (tablas) pertenecen a la base de datos y proporciona acceso a los DAOs.
-   **Patrón Singleton:** Se implementa dentro de un `companion object` para garantizar que solo exista **una única instancia** de la base de datos en toda la aplicación. Esto es crucial para el rendimiento y para evitar conflictos. La lógica `synchronized` y la anotación `@Volatile` aseguran que la creación de la instancia sea segura incluso en entornos con múltiples hilos.

---

## Capa de Lógica de Negocio

### 1. Repository (Repositorio) - `UserRepository.kt`

-   Una clase que aísla la capa de datos del resto de la aplicación.
-   Proporciona un API limpia para que el `ViewModel` acceda a los datos, sin que este último necesite saber si los datos vienen de una base de datos local o de una API remota.
-   Es el **único punto de verdad** (Single Source of Truth) para los datos.

### 2. ViewModel (Vista-Modelo) - `AuthViewModel.kt`

-   Recibe el `UserRepository` a través de su constructor (Inyección de Dependencias).
-   Contiene la lógica de negocio (ej. validar si un usuario ya existe, registrarlo).
-   Utiliza `viewModelScope` para lanzar corrutinas que se cancelan automáticamente cuando el ViewModel ya no está en uso, evitando fugas de memoria.
-   Comunica los resultados a la UI mediante callbacks (funciones lambda).

---

## Capa de UI (Interfaz de Usuario)

### 1. Jetpack Compose y State Hoisting

-   La UI se construye de forma declarativa con funciones `@Composable`.
-   Se utiliza `remember` con `mutableStateOf` para gestionar el estado de los componentes (ej. el texto en un `OutlinedTextField`).
-   **State Hoisting (Elevar el Estado):** En lugar de pasar el `NavController` completo a las pantallas, se pasan funciones lambda (ej. `onSignupSuccess: () -> Unit`). Esto hace que las pantallas sean más reutilizables, fáciles de probar y no dependan directamente de la lógica de navegación.

### 2. Navegación (Navigation Compose) - `AppNavigation.kt`

-   Se utiliza un `NavHost` para definir un grafo de navegación con diferentes rutas (`composable("ruta") { ... }`).
-   El `NavController` se utiliza para ejecutar las acciones de navegación (`navigate(...)`).
-   Se gestiona el historial de navegación (`back stack`) con `popUpTo { inclusive = true }` para ofrecer una experiencia de usuario correcta (ej. impedir que un usuario logueado vuelva a la pantalla de login).

---

## Cómo Probar la Base de Datos

Android Studio ofrece una herramienta integrada para inspeccionar la base de datos de tu aplicación en tiempo real:

1.  Ejecuta la aplicación en un emulador o dispositivo (API 26+).
2.  Abre la pestaña **App Inspection** en la parte inferior de Android Studio.
3.  Selecciona la pestaña **Database Inspector**.
4.  Elige tu proceso y la base de datos (`app_database.db`).

Desde allí, podrás ver las tablas, los datos que se guardan al registrar un usuario y hasta ejecutar consultas SQL personalizadas.
