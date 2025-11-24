package com.example.appshop.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.appshop.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PromotionsSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun promotionsSection_displaysBothPromotions() {

        // Espera a que la MainActivity termine de cargar su propia UI (AppNavigation).
        composeTestRule.waitForIdle()

        // 1. Arrange: Ahora que la actividad está estable, reemplazamos su contenido
        // de forma segura con el componente que queremos probar.
        composeTestRule.setContent {
            PromotionsSection()
        }

        // 2. Act & Assert: Buscamos los nodos de texto y verificamos que se muestren
        composeTestRule.onNodeWithText("Descuento en Alfajores").assertIsDisplayed()
        composeTestRule.onNodeWithText("Compra 6 alfajores y obtén un 10% de descuento.").assertIsDisplayed()

        composeTestRule.onNodeWithText("Combo Café y Queque").assertIsDisplayed()
        composeTestRule.onNodeWithText("Disfruta un café y un queque por solo $3.000.").assertIsDisplayed()
    }
}
