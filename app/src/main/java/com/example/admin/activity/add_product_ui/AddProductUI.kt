package com.example.admin.activity.add_product_ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.setMargins
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.admin.Network.Apis
import com.example.admin.R
import com.example.admin.utils.JSONHelper
import com.google.android.material.imageview.ShapeableImageView
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.add_product_ui.*
import kotlinx.android.synthetic.main.add_product_ui.view.*
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.android.synthetic.main.stock_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class AddProductUI : AppCompatActivity() {

    lateinit var viewModel: AddProductUIViewModel
    private var productSaved = false
    private var categoryWithNameList: AddProductUIViewModel.CategoryWithNameList? = null
    private var sizeWithNameList: AddProductUIViewModel.SizeWithNameList? = null
    private var selectedCategory = 0
    private var selectedSize = 0
    private var productId = -1
    private var descAdded = false
    private var currentImage = -1
    private var IMAGE_PICK_REQ = 999
    private var IMAGE_CAPTURE_REQ = 989
    private var images = ArrayList<String>()
    private val layoutParams = LinearLayoutCompat.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        this.setMargins(10)
    }
    private var pressedBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product_ui)

        viewModel = ViewModelProvider(this).get(AddProductUIViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            this.title = "Create Product"
            this.setDisplayHomeAsUpEnabled(true)
        }

        viewModel.categories.observe(this) {
            categoryWithNameList = it
            card1?.apply {
                this.card1SaveBtn.setOnClickListener {
                    if (productSaved) return@setOnClickListener
                    if (this.nameField.text.isNullOrEmpty()) return@setOnClickListener
                    if (this.brandField.text.isNullOrEmpty()) return@setOnClickListener
                    if (this.colorFieldP.text.isNullOrEmpty()) return@setOnClickListener
                    saveProduct(
                        this.nameField.text.toString(),
                        this.brandField.text.toString(),
                        this.colorFieldP.text.toString()
                    )
                }
                this.categorySpinner.apply {
                    this.adapter = ArrayAdapter(
                        this@AddProductUI,
                        android.R.layout.simple_spinner_dropdown_item,
                        it.nameList
                    )
                    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            selectedCategory = p2
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                    }
                }
            }
        }
        viewModel.productId.observe(this) {
            productId = it
            card1?.apply {
                this.card1SaveBtn.isEnabled = false
            }
            card2?.apply {
                this.card2SaveBtn.setOnClickListener {
                    if (this.unitField.text.isNullOrEmpty()) return@setOnClickListener
                    if (this.materialField.text.isNullOrEmpty()) return@setOnClickListener
                    if (this.colorField.text.isNullOrEmpty()) return@setOnClickListener
                    if (this.fitField.text.isNullOrEmpty()) return@setOnClickListener
                    if (this.sleeveField.text.isNullOrEmpty()) return@setOnClickListener
                    if (this.patternField.text.isNullOrEmpty()) return@setOnClickListener
                    if (this.descField.text.isNullOrEmpty()) return@setOnClickListener
                    viewModel.saveDesc(
                        productId,
                        JSONHelper.descObj(
                            this.unitField.text.toString(),
                            this.materialField.text.toString(),
                            this.colorField.text.toString(),
                            this.fitField.text.toString(),
                            this.sleeveField.text.toString(),
                            this.patternField.text.toString(),
                            this.descField.text.toString()
                        )
                    )
                }
            }
        }
        viewModel.descAdded.observe(this) {
            if (!it) return@observe
            descAdded = it
            card2.card2SaveBtn.isEnabled = false
            card3.apply {
                this.img1.setOnClickListener {
                    currentImage = 0
                    pickedDialog()
                }
                this.img2.setOnClickListener {
                    currentImage = 1
                    pickedDialog()
                }
                this.img3.setOnClickListener {
                    currentImage = 2
                    pickedDialog()
                }
                this.img4.setOnClickListener {
                    currentImage = 3
                    pickedDialog()
                }
                this.img5.setOnClickListener {
                    currentImage = 4
                    pickedDialog()
                }
                this.img6.setOnClickListener {
                    currentImage = 5
                    pickedDialog()
                }
            }
            card3.card3SaveBtn.setOnClickListener { v ->
                if (images.size > 0) {
                    viewModel.loadSizes()
                    v.isEnabled = false
                } else {
                    return@setOnClickListener
                }
            }
        }

        viewModel.sizes.observe(this) {
            sizeWithNameList = it
            card4?.apply {
                this.sizeSelector.apply {
                    this.adapter = ArrayAdapter(
                        this@AddProductUI,
                        android.R.layout.simple_spinner_dropdown_item,
                        it.sizes
                    )
                    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            selectedSize = p2
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }

                    }
                }
                this.stockAddBtn.setOnClickListener {
                    when {
                        this.stockField.text.isNullOrEmpty()
                                || this.mrpField.text.isNullOrEmpty()
                                || this.priceField.text.isNullOrEmpty()
                                || this.discountField.text.isNullOrEmpty() -> return@setOnClickListener
                        else -> viewModel.addStock(
                            productId,
                            stock = this.stockField.text.toString(),
                            mrp = this.mrpField.text.toString(),
                            price = this.priceField.text.toString(),
                            discount = this.discountField.text.toString(),
                            size = sizeWithNameList!!.sizes[selectedSize].id
                        )
                    }
                }
            }
        }
        viewModel.stockAdded.observe(this) {
            val view = LayoutInflater.from(this).inflate(R.layout.stock_view, null, false)
            view.apply {
                this.textView.text =
                    "Stock  - ${it.stock} piece , Size- ${it.sizeId} , Price Rs. ${it.price} , MRP Rs. ${it.mrp} with Discount ${it.discount}"
                this.layoutParams = this@AddProductUI.layoutParams
            }
            stockHolder?.addView(view)

            card4?.apply {
                this.discountField.text?.clear()
                this.priceField.text?.clear()
                this.mrpField.text?.clear()
                this.stockField.text?.clear()

            }
        }

        card3.apply {
            this.img1.setOnClickListener {
                currentImage = 0
                pickedDialog()
            }
            this.img2.setOnClickListener {
                currentImage = 1
                pickedDialog()
            }
            this.img3.setOnClickListener {
                currentImage = 2
                pickedDialog()
            }
            this.img4.setOnClickListener {
                currentImage = 3
                pickedDialog()
            }
            this.img5.setOnClickListener {
                currentImage = 4
                pickedDialog()
            }
            this.img6.setOnClickListener {
                currentImage = 5
                pickedDialog()
            }
        }


        viewModel.preload()
        /*GlobalScope.launch {
            delay(2000)
            runOnUiThread {
                viewModel.loadSizes()
            }
        }*/


    }


    private fun pickedDialog() {
        AlertDialog.Builder(this).apply {
            this.setTitle("Select an option")
            this.setItems(arrayOf("Open Camera", "Select from Gallery")) { _, i ->
                when (i) {
                    0 -> startCam()
                    1 -> startActivityForResult(
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        ), IMAGE_PICK_REQ
                    )
                }
            }
        }.also {
            it.create().show()
        }
    }

    private fun saveProduct(text: String, text1: String, text2: String) {
        viewModel.addProduct(
            text,
            text1,
            text2,
            categoryWithNameList!!.categories[selectedCategory]
        )
    }

    private fun startCam() {
        val imagesDir = File(filesDir, "images")
        imagesDir.mkdirs()
        val file = File(imagesDir, "captured.png")
        try {
            file.createNewFile()
        } catch (e: IOException) {
            Log.d("mridx", "openCamera: coudln't crate ")
            e.printStackTrace()
        }
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val authority = "com.example.admin.FileProvider"
        val outputUri = FileProvider.getUriForFile(
            applicationContext,
            authority,
            file
        )
        i.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        startActivityForResult(i, IMAGE_CAPTURE_REQ)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQ) {
            //Toast.makeText(context, "Image picked", Toast.LENGTH_SHORT).show()
            GlobalScope.launch {
                data?.data?.let {
                    CropImage.activity(it)
                        .start(this@AddProductUI)
                }
            }
        } else if (requestCode == IMAGE_CAPTURE_REQ && resultCode == RESULT_OK) {
            GlobalScope.launch {
                val uri = File(filesDir, "images/captured.png").toUri()
                CropImage.activity(uri)
                    .start(this@AddProductUI)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.getActivityResult(data).also {
                if (resultCode == RESULT_OK) {
                    it.uri.path?.let { it1 -> uploadImage(it1) }
                }
            }
        }
    }

    private fun uploadImage(it1: String) {
        GlobalScope.launch {
            try {
                OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS).build()
                    .newCall(
                        Request.Builder()
                            .url(Apis.imageUpload)
                            .addHeader("Accept", "application/json")
                            .post(
                                MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart(
                                        "image",
                                        File(it1).name,
                                        RequestBody.create(
                                            "image/jpg".toMediaTypeOrNull(),
                                            File(it1)
                                        )
                                    )
                                    //.addFormDataPart("product_id", 21.toString())
                                    .addFormDataPart("product_id", productId.toString())
                                    .build()
                            )
                            .build()
                    ).execute().use {
                        if (it.isSuccessful) {
                            showImageAndSave(it.body?.string())
                            return@use
                        }
                        showFailed(it.body?.string())
                    }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showImageAndSave(string: String?) {
        string ?: return
        try {
            val obj = JSONObject(string)
            obj.getJSONObject("img").getString("url").also {
                runOnUiThread {
                    images.add(it)
                    addImage(it)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun addImage(it: String) {
        when (currentImage) {
            0 -> loadTo(img1, it)
            1 -> loadTo(img2, it)
            2 -> loadTo(img3, it)
            3 -> loadTo(img4, it)
            4 -> loadTo(img5, it)
            5 -> loadTo(img6, it)
        }
    }

    private fun loadTo(img: ShapeableImageView?, name: String) {
        img?.let {
            Glide.with(this).asBitmap().load("${Apis.liveImage}$name").into(it)
        }
    }

    private fun showFailed(string: String?) {
        runOnUiThread {
            Toast.makeText(this, "Failed ! ${getMessage(string)}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getMessage(string: String?): String {
        try {
            string ?: throw JSONException("string is null")
            val obj = JSONObject(string)
            if (obj.has("error") && obj.getJSONObject("error").has("message")) {
                return obj.getJSONObject("error").getString("message")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (productId == -1 || pressedBack)
            super.onBackPressed()
        else {
            Toast.makeText(
                this,
                "Complete Creating product or back press again to close",
                Toast.LENGTH_SHORT
            ).show()
            pressedBack = true
            GlobalScope.launch {
                delay(1000 * 2)
                pressedBack = false
            }
        }
    }

}