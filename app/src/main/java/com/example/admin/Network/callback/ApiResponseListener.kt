package com.example.admin.Network.callback

import org.json.JSONArray
import org.json.JSONObject

interface ApiResponseListener {

    fun onApiResponse(
        success: Boolean,
        type: RequestType,
        responseObj: JSONObject?,
        responseArr: JSONArray?
    )

}