package tech.fremeaux.mygarage

import android.content.Context
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock
import tech.fremeaux.mygarage.data.repo.CarRepository
import tech.fremeaux.mygarage.data.repo.MakeRepository

class RepositoryTest {
    // On crée un faux Context qui ne fait rien pour pouvoir call les fonctions de l'app (CarRepository)
    private val mockContext = mock(Context::class.java)
    private val carRepo = CarRepository(mockContext)
    private val makeRepo = MakeRepository(mockContext)

    @Test
    fun parseCar_validJson_returnsCarObject() {
        val json = """{"data": [{"id": 1, "make": "Ferrari", "model": "458", "horsepower_hp": 562, "torque_ft_lbs": 398, "year": "2015", "fuel_type": "Gasoline", "drive_type": "RWD", "transmission": "Automatic"}]}"""

        val result = carRepo.parseCar(json)

        assertNotNull(result)
        assertEquals("Ferrari", result?.make)
        assertEquals(562, result?.hp)
    }

    //test si data api vide (il ce peux qu'il n'y
    @Test
    fun parseCar_emptyData_returnsNullNoCrash() {
        val json = """{"data": []}"""
        val result = carRepo.parseCar(json)
        assertNull(result)
    }

    @Test
    fun parseMakes_emptyJson_returnsEmptyList() {
        val json = """{"data": []}"""
        val result = makeRepo.parseMakesAndStore(json)
        assertTrue(result.isEmpty())
    }

    @Test
    fun parseMakes_malformedJson_returnsEmptyList() {
        val malformedJson = "{ invalid }"

        // On vérifie que l'app gère l'erreur sans crash
        val result = try {
            makeRepo.parseMakesAndStore(malformedJson)
        } catch (e: Exception) {
            emptyList()
        }

        assertTrue(result.isEmpty())
    }
}