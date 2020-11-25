package com.example.admin.Network

import com.android.volley.DefaultRetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.admin.Network.callback.ApiResponseListener
import com.example.admin.Network.callback.RequestType
import org.json.JSONException
import org.json.JSONObject

class ApiHandler {

    var apiResponseListener: ApiResponseListener? = null

    private val defaultRetryPolicy = DefaultRetryPolicy(
        0,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )

    companion object {
        val _instance = ApiHandler()
        /*fun getInstance() : ApiHandler {
            if (_instance == null)
                _instance = ApiHandler()
            return _instance as ApiHandler
        }*/
    }

    private fun parseError(it: VolleyError?): JSONObject? {
        if (it!!.networkResponse == null) return null
        try {
            return JSONObject(String(it.networkResponse.data))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null

    }

    fun categories() {
        val arrayRequest = object : JsonArrayRequest(
            Method.GET,
            Apis.categories,
            null,
            { apiResponseListener?.onApiResponse(true, RequestType.CATEGORIES, null, it) },
            {
                apiResponseListener?.onApiResponse(
                    false,
                    RequestType.CATEGORIES,
                    parseError(it),
                    null
                )
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                //headers["Authorization"] = LiveSession.getInstance().getUserToken()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        arrayRequest.retryPolicy = defaultRetryPolicy
        VolleySingleton.instance?.addToRequestQueue(arrayRequest)
    }

    fun addProduct(params: JSONObject) {
        val objectRequest = object : JsonObjectRequest(
            Method.POST,
            Apis.addProduct,
            params,
            { apiResponseListener?.onApiResponse(true, RequestType.ADD_PRODUCT, it, null) },
            {
                apiResponseListener?.onApiResponse(
                    false,
                    RequestType.ADD_PRODUCT,
                    parseError(it),
                    null
                )
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    this["Accept"] = "application/json"
                }
            }
        }
        objectRequest.retryPolicy = defaultRetryPolicy
        VolleySingleton.instance?.addToRequestQueue(objectRequest)
    }

    fun addProductDesc(productId: Int, params: JSONObject) {
        val objectRequest = object : JsonObjectRequest(
            Method.POST,
            "${Apis.productDesc}$productId",
            params,
            { apiResponseListener?.onApiResponse(true, RequestType.PRODUCT_DESC, it, null) },
            {
                apiResponseListener?.onApiResponse(
                    false,
                    RequestType.PRODUCT_DESC,
                    parseError(it),
                    null
                )
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    this["Accept"] = "application/json"
                }
            }
        }
        objectRequest.retryPolicy = defaultRetryPolicy
        VolleySingleton.instance?.addToRequestQueue(objectRequest)
    }

    fun sizes() {
        val arrayRequest = object : JsonArrayRequest(
            Method.GET,
            Apis.sizes,
            null,
            { apiResponseListener?.onApiResponse(true, RequestType.SIZES, null, it) },
            { apiResponseListener?.onApiResponse(false, RequestType.SIZES, parseError(it), null) }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    this["Accept"] = "application/json"
                }
            }
        }
        arrayRequest.retryPolicy = defaultRetryPolicy
        VolleySingleton.instance?.addToRequestQueue(arrayRequest)
    }

    fun addStock(params: JSONObject) {
        val objectRequest = object : JsonObjectRequest(
            Method.POST,
            Apis.addStock,
            params,
            { apiResponseListener?.onApiResponse(true, RequestType.ADD_SIZE, it, null) },
            {
                apiResponseListener?.onApiResponse(
                    false,
                    RequestType.ADD_SIZE,
                    parseError(it),
                    null
                )
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    this["Accept"] = "application/json"
                }
            }
        }
        objectRequest.retryPolicy = defaultRetryPolicy
        VolleySingleton.instance?.addToRequestQueue(objectRequest)
    }

    fun createCategory(params: JSONObject) {
        val objectRequest = object : JsonObjectRequest(
            Method.POST,
            Apis.addCategory,
            params,
            { apiResponseListener?.onApiResponse(true, RequestType.ADD_CATEGORY, it, null) },
            {
                apiResponseListener?.onApiResponse(
                    false,
                    RequestType.ADD_CATEGORY,
                    parseError(it),
                    null
                )
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply {
                    this["Accept"] = "application/json"
                    this["Authorization"] = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjIsImlhdCI6MTYwNjI5MTMzNn0.EJ2qKBieEGCysbcNPSa7nzzq-rh80Su2Eh-KNbmNpV8"
                }
            }
        }
        objectRequest.retryPolicy = defaultRetryPolicy
        VolleySingleton.instance?.addToRequestQueue(objectRequest)
    }

}