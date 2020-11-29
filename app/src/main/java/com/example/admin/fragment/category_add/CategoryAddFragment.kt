package com.example.admin.fragment.category_add

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.admin.Network.Apis
import com.example.admin.R
import com.example.admin.utils.JSONHelper
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.category_add.*
import kotlinx.android.synthetic.main.category_add.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class CategoryAddFragment : DialogFragment() {

    private var image: String? = null

    lateinit var viewModel: CategoryAddFragmentViewModel

    private var IMAGE_PICK_REQ = 999
    private var IMAGE_CAPTURE_REQ = 989

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.category_add, container, false)
        dialog?.window?.attributes?.gravity = Gravity.FILL_HORIZONTAL
        viewModel =
            ViewModelProvider(this).get(CategoryAddFragmentViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.failed.observe(this) {
            if (it) {
                Toast.makeText(context!!, "Failed to add category , try again", Toast.LENGTH_LONG)
                    .show()
            }
        }
        viewModel.categoryAdded.observe(this) {
            if (it) {
                Toast.makeText(context!!, "Category Added", Toast.LENGTH_LONG).show()
            }
            GlobalScope.launch {
                delay(1000*2)
                this@CategoryAddFragment.dismiss()
            }
        }

        view.apply {
            this.imageView.setOnClickListener {
                pickedDialog()
            }
            this.categorySaveBtn.setOnClickListener {
                if (this.nameField.text.isNullOrEmpty()) return@setOnClickListener
                if (image == null) return@setOnClickListener
                viewModel.createCategory(
                    JSONHelper.categoryObj(
                        this.nameField.text.toString(),
                        image!!
                    )
                )
            }
        }

    }

    private fun pickedDialog() {
        AlertDialog.Builder(context!!).apply {
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

    private fun startCam() {
        val imagesDir = File(context!!.filesDir, "images")
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
            context!!.applicationContext,
            authority,
            file
        )
        i.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        startActivityForResult(i, IMAGE_CAPTURE_REQ)

    }

    private fun uploadImage(it1: String) {
        GlobalScope.launch {
            try {
                OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS).build()
                    .newCall(
                        Request.Builder()
                            .url(Apis.uploadImage)
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
                                    //.addFormDataPart("product_id", productId.toString())
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
            obj.getString("fileName").also { it ->
                this.image = it
                this.activity?.let { act ->
                    act.runOnUiThread {
                        Glide.with(this).asBitmap().load("${Apis.tmpImage}$it").into(imageView)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showFailed(string: String?) {
        this.activity?.let {
            it.runOnUiThread {
                Toast.makeText(context, "Failed ! ${getMessage(string)}", Toast.LENGTH_LONG).show()
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQ) {
            //Toast.makeText(context, "Image picked", Toast.LENGTH_SHORT).show()
            GlobalScope.launch {
                data?.data?.let {
                    CropImage.activity(it)
                        .start(context!!, this@CategoryAddFragment)
                }
            }
        } else if (requestCode == IMAGE_CAPTURE_REQ && resultCode == AppCompatActivity.RESULT_OK) {
            GlobalScope.launch {
                val uri = File(context!!.filesDir, "images/captured.png").toUri()
                CropImage.activity(uri)
                    .start(activity!!)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.getActivityResult(data).also {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    it.uri.path?.let { it1 -> uploadImage(it1) }
                }
            }
        }
    }

}