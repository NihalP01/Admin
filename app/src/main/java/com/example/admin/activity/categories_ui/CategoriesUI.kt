package com.example.admin.activity.categories_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin.Adapters.CategoriesAdapter
import com.example.admin.R
import com.example.admin.fragment.category_add.CategoryAddFragment
import kotlinx.android.synthetic.main.categories_ui.*

class CategoriesUI : AppCompatActivity() {

    lateinit var viewModel: CategoriesUIViewModel
    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories_ui)


        viewModel = ViewModelProvider(this).get(CategoriesUIViewModel::class.java)

        viewModel.categories.observe(this) {
            categoriesAdapter?.setList(it)
        }


        categoriesAdapter = CategoriesAdapter()
        categoriesHolder?.apply {
            this.adapter = categoriesAdapter
            this.layoutManager = LinearLayoutManager(this@CategoriesUI)
        }


        fabBtn?.setOnClickListener {
            CategoryAddFragment().apply {
                this.show(supportFragmentManager, "Category add")
            }
        }

        viewModel.loadCategories()

    }


}