package com.example.admin.activity.add_product_ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.admin.Network.Apis
import com.example.admin.R
import com.example.admin.utils.JSONHelper
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.add_product_ui.*
import kotlinx.android.synthetic.main.add_product_ui.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class AddProductUI : AppCompatActivity() {

    lateinit var viewModel: AddProductUIViewModel
    private var productSaved = false
    private var categoryWithNameList: AddProductUIViewModel.CategoryWithNameList? = null
    private var selectedCategory = 0
    private var productId = -1
    private var descAdded = false
    private var currentImage = -1
    private var IMAGE_PICK_REQ = 999
    private var IMAGE_CAPTURE_REQ = 989

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product_ui)

        viewModel = ViewModelProvider(this).get(AddProductUIViewModel::class.java)

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
                                    .build()
                            )
                            .build()
                    ).execute().use {
                        Log.d("kaku", "uploadImage: ${it.body?.string()}")
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}