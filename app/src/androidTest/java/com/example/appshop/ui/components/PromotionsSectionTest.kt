package com.example.appshop.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) //Se usará un ejecutor de Android, que proporciona el entorno necesario para las pruebas de UI.
class PromotionsSectionTest {

    /**
     * `createComposeRule()` crea un entorno de prueba para Composables sin necesidad de iniciar una Activity completa.
     * `composeTestRule` es el objeto que usaremos para interactuar con la UI en nuestras pruebas.
     */
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Esta es la función de prueba. La anotación `@Test` le indica a JUnit que este método debe ejecutarse como una prueba.
     * El nombre del método describe lo que se está probando: que la sección de promociones muestra ambas promociones.
     */
    @Test
    fun promotionsSection_displaysBothPromotions() {

        // `setContent` renderiza el Composable que queremos probar dentro del entorno de prueba.
        composeTestRule.setContent {
            PromotionsSection() // Componente a probar.
        }

        // Se busca un nodo que contenga el texto dentro de .onNodeWithText().
        // `assertIsDisplayed()` verifica que el nodo encontrado esté visible en la pantalla.
        // Si no se encuentra el nodo o no está visible, la prueba fallará.
        composeTestRule.onNodeWithText("Descuento en Alfajores").assertIsDisplayed()
        composeTestRule.onNodeWithText("Compra 6 alfajores y obtén un 10% de descuento.").assertIsDisplayed()

        // Se repite el proceso para la segunda promoción.
        composeTestRule.onNodeWithText("Combo Café y Queque").assertIsDisplayed()
        composeTestRule.onNodeWithText("Disfruta un café y un queque por solo $3.000.").assertIsDisplayed()
    }
}
