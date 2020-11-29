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

        fun parseSizes(responseArr: JSONArray): ArrayList<Sizes> {
            val list = ArrayList<Sizes>()
            for (i in 0 until responseArr.length()) {
                list.add(parseSize(responseArr[i] as JSONObject))
            }
            return list
        }

        private fun parseSize(responseObj: JSONObject): Sizes {
            return Sizes(
                responseObj.getInt("id"),
                responseObj.getString("name")
            )
        }

        fun getSizeNameList(list: ArrayList<Sizes>): ArrayList<String> {
            val l = ArrayList<String>()
            list.forEach {
                l.add(it.name)
            }
            return l
        }

        fun stockObj(
            productId: Int,
            stock: String,
            mrp: String,
            price: String,
            discount: String,
            size: Int
        ): JSONObject {
            return JSONObject().apply {
                this.put("product_id", productId)
                this.put("size_id", size)
                this.put("stock", stock)
                this.put("price", price)
                this.put("mrp", mrp)
                this.put("discount", discount)
            }
        }

        fun parseStock(it: JSONObject): Stock {
            val stock = it.getJSONObject("stock")
            return Stock(
                stock.getInt("id"),
                stock.getInt("size_id"),
                stock.getInt("stock"),
                stock.getString("price"),
                stock.getString("mrp"),
                stock.getString("discount")
            )
        }

        fun categoryObj(name: String, image: String): JSONObject {
            return JSONObject().apply {
                this.put("name", name)
                this.put("image", image)
            }
        }

    }

}