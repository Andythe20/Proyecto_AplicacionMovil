package com.example.appshop.viewmodel

import com.example.appshop.model.Product
// Importamos las aserciones (verificaciones) de Kotest porque son más legibles, pero el test se ejecutará con JUnit
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
// Importaciones clave de JUnit 5
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    // Creamos un "controlador de tiempo" para nuestras corutinas
    private val testDispatcher = StandardTestDispatcher()

    // Declaramos una variable para nuestro ViewModel
    private lateinit var viewModel: CartViewModel

    // @BeforeEach se ejecuta ANTES de cada test. Es el equivalente al `beforeTest` de Kotest.
    @BeforeEach
    fun setUp() {
        // Le decimos a las corutinas de Android que usen nuestro "controlador de tiempo"
        Dispatchers.setMain(testDispatcher)
        // Creamos una instancia NUEVA y LIMPIA del ViewModel
        viewModel = CartViewModel()
    }

    // @AfterEach se ejecuta DESPUÉS de cada test. Es el equivalente al `afterTest` de Kotest.
    @AfterEach
    fun tearDown() {
        // Devolvemos el control del tiempo a su estado normal
        Dispatchers.resetMain()
    }


    /* ===== TESTS ===== */

    // @Test le dice a JUnit que esta función es un test ejecutable.
    @Test
    // @DisplayName permite dar una descripción legible al test.
    @DisplayName("test 1: cuando el ViewModel se crea, el carrito debe estar vacío y el total debe ser cero")
    fun `test 1, el carrito inicia vacio y el total es cero`() = runTest {
        viewModel.productosEnCarrito.value.shouldBeEmpty()
        viewModel.total.value shouldBe 0
    }

    @Test
    @DisplayName("test 2: al agregar dos productos, el carrito debe tener dos items y el total debe actualizarse")
    fun `test 2, al agregar productos el carrito y total se actualizan`() = runTest {
        val productoDePrueba1 = Product(
            codigo = "001",
            nombre = "Flan",
            descripcion = "Rico flan de prueba",
            categoria = "Flan PremiumVIP_OMG",
            precio = 500000,
            url = ""
        )

        val productoDePrueba2 = Product(
            codigo = "002",
            nombre = "Torta",
            descripcion = "Rica torta de prueba",
            categoria = "Torta PremiumVIP_OMG",
            precio = 1,
            url = ""
        )

        // ACCIÓN (When)
        viewModel.agregarProducto(productoDePrueba1)
        viewModel.agregarProducto(productoDePrueba2)


        // RESULTADO (Then)
        val carritoActual = viewModel.productosEnCarrito.value
        carritoActual.shouldHaveSize(2)
        carritoActual[productoDePrueba1] shouldBe 1
        carritoActual[productoDePrueba2] shouldBe 1
        viewModel.total.value shouldBe 500001
    }

    @Test
    @DisplayName("test 3: al eliminar un producto, el carrito debe quedar vacío")
    fun `test 3, al eliminar un producto el carrito queda vacio`() = runTest {
        // Given
        val productoDePrueba = Product(codigo="001", nombre="Flan", descripcion="", categoria="", precio=5, url="")
        viewModel.agregarProducto(productoDePrueba)

        // When
        viewModel.eliminarProducto(productoDePrueba)

        // Then
        viewModel.productosEnCarrito.value.shouldBeEmpty()
        viewModel.total.value shouldBe 0
    }

    @Test
    @DisplayName("test 4: al cambiar la cantidad, el carrito y el total deben actualizarse")
    fun `test 4, al cambiar la cantidad el carrito y total se actualizan`() = runTest {
        // Given
        val productoDePrueba = Product(codigo="001", nombre="Flan", descripcion="", categoria="", precio=5, url="")
        viewModel.agregarProducto(productoDePrueba) // Ahora hay 1 flan, total = 5

        // When
        viewModel.cambiarCantidad(productoDePrueba, 3)

        // Then
        viewModel.productosEnCarrito.value[productoDePrueba] shouldBe 3
        viewModel.total.value shouldBe 15
    }
}
