package tech.fremeaux.mygarage.data.repo

import android.content.ContentValues
import android.content.Context
import tech.fremeaux.mygarage.data.DataBase
import tech.fremeaux.mygarage.data.model.Car

class CarRepository(context: Context) {

    private val dbHelper = DataBase(context)

    fun addCar(make: String, model: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("make", make)
            put("model", model)
        }

        db.insert("cars", null, values)
        db.close()
    }

    fun getCars(): List<Car> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cars", null)

        val cars = mutableListOf<Car>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val make = cursor.getString(cursor.getColumnIndexOrThrow("make"))
            val model = cursor.getString(cursor.getColumnIndexOrThrow("model"))

            cars.add(Car(id, make, model))
        }

        cursor.close()
        db.close()

        return cars
    }

    fun updateCar(id:Int, name: String, details: String, priority: String){
        val db = dbHelper.readableDatabase
        val values = ContentValues().apply {
            put("make", name)
            put("model", details)
        }

        db.update("cars", values, "id = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteCar(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete("cars", "id = ?", arrayOf(id.toString()))
        db.close()
    }
}