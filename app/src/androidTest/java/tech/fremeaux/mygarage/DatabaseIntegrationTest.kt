package tech.fremeaux.mygarage

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import tech.fremeaux.mygarage.data.repo.CarRepository

@RunWith(AndroidJUnit4::class)
class DatabaseIntegrationTest {

    //test si une voiture ajouter marche bien
    @Test
    fun testAddAndRetrieveCarFromGarage() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repo = CarRepository(context)

        repo.addCar("TestMake",1, "TestModel", 1, 500, 400, 0L, "2024", "Electric", "AWD", "Auto")

        val garage = repo.getCars()

        val savedCar = garage.find { it.model == "TestModel" }
        assertNotNull(savedCar)
        assertEquals("TestMake", savedCar?.make)
    }
}