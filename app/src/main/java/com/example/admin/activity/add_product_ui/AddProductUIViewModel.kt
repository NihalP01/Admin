package com.example.admin.activity.add_product_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin.Network.ApiHandler
import com.example.admin.Network.DataClasses
import com.example.admin.Network.callback.ApiResponseListener
import com.example.admin.Network.callback.RequestType
import com.example.admin.utils.JSONHelper
import org.json.JSONArray
import org.json.JSONObject

class AddProductUIViewModel : ViewModel(), ApiResponseListener {

    lateinit var apiHandler: ApiHandler

    data class CategoryWithNameList(
        var categories: ArrayList<DataClasses.Categories>,
        var nameList: ArrayList<String>
    )

    data class SizeWithNameList(
        var sizes: ArrayList<DataClasses.Sizes>,
        var nameList: ArrayList<String>
    )

    private var _categories = MutableLiveData<CategoryWithNameList>()
    val categories: LiveData<CategoryWithNameList> = _categories

    private var _sizes = MutableLiveData<SizeWithNameList>()
    val sizes: LiveData<SizeWithNameList> = _sizes

    private var _productId = MutableLiveData<Int>()
    val productId: LiveData<Int> = _productId

    private var _descAdded = MutableLiveData<Boolean>()
    val descAdded: LiveData<Boolean> = _descAdded

    private var _stockAdded = MutableLiveData<DataClasses.Stock>()
    val stockAdded: LiveData<DataClasses.Stock> = _stockAdded


    fun preload() {
        apiHandler = ApiHandler._instance
        apiHandler.apply {
            this.apiResponseListener = this@AddProductUIViewModel
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
            //failed
            return
        }
        when (type) {
            RequestType.CATEGORIES -> {
                responseArr?.let {
                    JSONHelper.parseCategories(it).also { l ->
                        _categories.postValue(
                            CategoryWithNameList(
                                l,
                                JSONHelper.getCategoryNameList(l)
                            )
                        )
                    }
                }
            }
            RequestType.ADD_PRODUCT -> {
                responseObj?.let {
                    _productId.postValue(it.getJSONObject("product").getInt("id"))
                }
            }
            RequestType.PRODUCT_DESC -> {
                _descAdded.postValue(true)
            }
            RequestType.SIZES -> {
                responseArr?.let {
                    JSONHelper.parseSizes(it).also { l ->
                        _sizes.postValue(
                            SizeWithNameList(
                                l,
                                JSONHelper.getSizeNameList(l)
                            )
                        )
                    }

                }
            }
            RequestType.ADD_SIZE -> {
                responseObj?.let {
                    _stockAdded.postValue(
                        JSONHelper.parseStock(it)
                    )
                }
            }
        }
    }

    fun addProduct(text: String, text1: String, text2: String, categories: DataClasses.Categories) {
        JSONObject().apply {
            this.put("name", text)
            this.put("brand_name", text1)
            this.put("color", text2)
            this.put("category_id", categories.id)
        }.also {
            apiHandler.addProduct(it)
        }
    }

    fun saveDesc(productId: Int, params: JSONObject) {
        apiHandler.addProductDesc(productId, params)
    }

    fun loadSizes() {
        apiHandler.sizes()
    }

    fun addStock(
        productId: Int,
        stock: String,
        mrp: String,
        price: String,
        discount: String,
        size: Int
    ) {
        apiHandler.addStock(JSONHelper.stockObj(productId, stock, mrp, price, discount, size))
    }
}