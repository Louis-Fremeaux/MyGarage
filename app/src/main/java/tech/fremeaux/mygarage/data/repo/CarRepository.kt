package tech.fremeaux.mygarage.data.repo

import android.content.ContentValues
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import org.json.JSONObject
import tech.fremeaux.mygarage.data.CarTableName
import tech.fremeaux.mygarage.data.DataBase
import tech.fremeaux.mygarage.data.model.Car
import tech.fremeaux.mygarage.data.model.Model

class CarRepository(context: Context) {
    private val dbHelper = DataBase(context)

    fun parseCar(json: String): Car? {
        if (!json.isEmpty()){
            val list = mutableListOf<Car>()
            val jsonArray = JSONObject(json).getJSONArray("data")

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val car = Car(id = obj.getInt("id"), make = obj.getString("make"),model = obj.getString("model"), hp = obj.getInt("horsepower_hp"), color = Color(255,255,255).toColorLong())
                list.add(car)
            }
            return list.first()
        }else{
            return null
        }
    }

    fun addCar(make:String, model:String, hp:Int, color:Long) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("make", make)
            put("model", model)
            put("hp", hp)
            put("color", color)
        }

        db.insert(CarTableName, null, values)
        db.close()
    }

    fun getCars(): List<Car> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $CarTableName", null)

        val cars = mutableListOf<Car>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val make = cursor.getString(cursor.getColumnIndexOrThrow("make"))
            val model = cursor.getString(cursor.getColumnIndexOrThrow("model"))
            val hp = cursor.getInt(cursor.getColumnIndexOrThrow("hp"))
            val color = cursor.getLong(cursor.getColumnIndexOrThrow("color"))

            cars.add(Car(id, make, model, hp, color))
        }

        cursor.close()
        db.close()

        return cars
    }

    fun updateCar(id:Int, make:String, model:String, hp:Int, color:Long){
        val db = dbHelper.readableDatabase
        val values = ContentValues().apply {
            put("make", make)
            put("model", model)
            put("hp", hp)
            put("color", color)
        }

        db.update(CarTableName, values, "id = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteCar(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete(CarTableName, "id = ?", arrayOf(id.toString()))
        db.close()
    }
}