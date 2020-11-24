package com.example.admin.utils

import com.example.admin.Network.DataClasses.*
import org.json.JSONArray
import org.json.JSONObject

class JSONHelper {

    companion object {

        fun parseCategories(responseArr: JSONArray): ArrayList<Categories> {
            val list = ArrayList<Categories>()
            for (i in 0 until responseArr.length()) {
                list.add(parseCategory(responseArr[i] as JSONObject))
            }
            return list
        }

        fun parseCategory(responseObj: JSONObject): Categories {
            return Categories(
                responseObj.getInt("id"),
                responseObj.getString("name")
            )
        }

        fun getCategoryNameList(list: ArrayList<Categories>): ArrayList<String> {
            val l = ArrayList<String>()
            list.forEach {
                l.add(it.name)
            }
            return l
        }

        fun descObj(
            unit: String,
            material: String,
            color: String,
            fit: String,
            sleeve: String,
            pattern: String,
            desc: String
        ): JSONObject {
            return JSONObject().apply {
                this.put("unit", unit)
                this.put("material", material)
                this.put("color", color)
                this.put("fit", fit)
                this.put("sleeve", sleeve)
                this.put("pattern", pattern)
                this.put("description", desc)
            }
        }

    }

}