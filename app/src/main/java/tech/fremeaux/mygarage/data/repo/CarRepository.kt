package tech.fremeaux.mygarage.data.repo

import android.content.ContentValues
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import org.json.JSONObject
import tech.fremeaux.mygarage.data.CarTableName
import tech.fremeaux.mygarage.data.DataBase
import tech.fremeaux.mygarage.data.model.Car

class CarRepository(context: Context) {
    private val dbHelper = DataBase(context)

    fun parseCar(json: String): Car? {
        if (!json.isEmpty()){
            val list = mutableListOf<Car>()
            val jsonArray = JSONObject(json).getJSONArray("data")

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val car = Car(
                    id = obj.getInt("id"),
                    make = obj.getString("make"),
                    makeId = 0,
                    model = obj.getString("model"),
                    modelId = 0,
                    hp = obj.getInt("horsepower_hp"),
                    nm= obj.getInt("torque_ft_lbs"),
                    color = Color(255,255,255).toColorLong(),
                    year = obj.getString("year"),
                    fuel= obj.getString("fuel_type"),
                    drive= obj.getString("drive_type"),
                    transmission=obj.getString("transmission"),
                    km=0
                )
                list.add(car)
            }
            return list.firstOrNull()
        }else{
            return null
        }
    }

    fun addCar(make:String, makeId:Int, model:String, modelId:Int, hp:Int, nm:Int, color:Long, year:String, fuel:String, drive:String, transmission:String, km:Int=0) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("make", make)
            put("makeId", makeId)
            put("model", model)
            put("modelId", modelId)
            put("hp", hp)
            put("nm", nm)
            put("color", color)
            put("year", year)
            put("fuel", fuel)
            put("drive", drive)
            put("transmission", transmission)
            put("km", km)
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
            val makeId = cursor.getInt(cursor.getColumnIndexOrThrow("makeId"))
            val model = cursor.getString(cursor.getColumnIndexOrThrow("model"))
            val modelId = cursor.getInt(cursor.getColumnIndexOrThrow("modelId"))
            val hp = cursor.getInt(cursor.getColumnIndexOrThrow("hp"))
            val nm = cursor.getInt(cursor.getColumnIndexOrThrow("nm"))
            val color = cursor.getLong(cursor.getColumnIndexOrThrow("color"))
            val year = cursor.getString(cursor.getColumnIndexOrThrow("year"))
            val fuel = cursor.getString(cursor.getColumnIndexOrThrow("fuel"))
            val drive = cursor.getString(cursor.getColumnIndexOrThrow("drive"))
            val transmission = cursor.getString(cursor.getColumnIndexOrThrow("transmission"))
            val km = cursor.getInt(cursor.getColumnIndexOrThrow("km"))

            cars.add(Car(id, make, makeId, model, modelId, hp, nm, color, year, fuel, drive, transmission, km))
        }

        cursor.close()
        db.close()

        return cars
    }

    fun deleteCar(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete(CarTableName, "id = ?", arrayOf(id.toString()))
        db.close()
    }
}