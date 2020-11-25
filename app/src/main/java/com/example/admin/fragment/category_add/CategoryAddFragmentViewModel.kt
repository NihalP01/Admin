package com.example.admin.fragment.category_add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin.Network.ApiHandler
import com.example.admin.Network.callback.ApiResponseListener
import com.example.admin.Network.callback.RequestType
import org.json.JSONArray
import org.json.JSONObject

class CategoryAddFragmentViewModel : ViewModel(), ApiResponseListener {

    lateinit var apiHandler: ApiHandler

    private var _categoryAdded = MutableLiveData<Boolean>()
    var categoryAdded : LiveData<Boolean> = _categoryAdded

    private var _failed = MutableLiveData<Boolean>()
    var failed : LiveData<Boolean> = _failed


    fun createCategory(params: JSONObject) {
        apiHandler = ApiHandler._instance
        apiHandler.apply {
            this.apiResponseListener = this@CategoryAddFragmentViewModel
            this.createCategory(params)

        }
    }

    override fun onApiResponse(
        success: Boolean,
        type: RequestType,
        responseObj: JSONObject?,
        responseArr: JSONArray?
    ) {

        if (!success) {
            _failed.postValue(true)
            return
        }
        when(type) {
            RequestType.ADD_CATEGORY -> {
                _categoryAdded.postValue(true)
            }
            else -> {}
        }

    }


}