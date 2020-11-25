package com.example.admin.activity.categories_ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin.Network.ApiHandler
import com.example.admin.Network.DataClasses
import com.example.admin.Network.callback.ApiResponseListener
import com.example.admin.Network.callback.RequestType
import com.example.admin.activity.add_product_ui.AddProductUIViewModel
import com.example.admin.utils.JSONHelper
import org.json.JSONArray
import org.json.JSONObject

class CategoriesUIViewModel : ViewModel(), ApiResponseListener {

    lateinit var apiHandler: ApiHandler

    private var _categories = MutableLiveData<ArrayList<DataClasses.Categories>>()
    val categories: LiveData<ArrayList<DataClasses.Categories>> = _categories

    fun loadCategories() {
        apiHandler = ApiHandler._instance
        apiHandler.apply {
            this.apiResponseListener = this@CategoriesUIViewModel
            this.categories()
        }
    }

    override fun onApiResponse(
        success: Boolean,
        type: RequestType,
        responseObj: JSONObject?,
        responseArr: JSONArray?
    ) {
        if (!success) {
            Log.d("kaku", "onApiResponse: $responseObj")
            return
        }
        when (type) {
            RequestType.CATEGORIES -> {
                responseArr?.let {
                    JSONHelper.parseCategories(it).also { l ->
                        _categories.postValue(
                            l
                        )
                    }
                }

            }
            else -> {}
        }
    }


}