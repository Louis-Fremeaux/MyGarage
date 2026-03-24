package tech.fremeaux.mygarage.data.model

import org.json.JSONObject

data class Model(val id:Int, val name:String)


fun parseModels(json: String): List<Model> {
    if (!json.isEmpty()){
        val list = mutableListOf<Model>()
        val jsonArray = JSONObject(json).getJSONArray("data")

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            val model = Model(id = obj.getInt("id"), name = obj.getString("name"))
            list.add(model)
            //addMake(obj.getString("name"))    AndStore pour le nom de la fonction
        }
        return list
    }else{
        return emptyList()
    }
}