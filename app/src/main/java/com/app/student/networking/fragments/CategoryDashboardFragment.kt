package com.app.student.networking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.student.networking.CategoryCallback
import com.app.student.networking.R
import com.app.student.networking.adapter.CategoryGridAdapter
import com.app.student.networking.databinding.CategoryGridRecyclerViewBinding
import com.app.student.networking.model.CategoryGridData
import com.app.student.networking.viewmodel.CategoryDashboardViewModel

class CategoryDashboardFragment : Fragment() , CategoryCallback{

    lateinit var binding : CategoryGridRecyclerViewBinding
    lateinit var adapter : CategoryGridAdapter
    lateinit var viewModel : CategoryDashboardViewModel

    companion object{
        const val TAG : String = "CategoryDashboardFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.category_grid_recycler_view, container, false)
        viewModel = ViewModelProvider(this).get(CategoryDashboardViewModel::class.java)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Categories"

        adapter = CategoryGridAdapter(viewModel.list , this)
        val recyclerView: RecyclerView = binding.categoryRecyclerView
        val gridLayoutManager = GridLayoutManager(activity,2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter
        
        return binding.root
    }

    override fun categorySelected(category: CategoryGridData) {
       // CategoryDashboardFragmentDirections.actionCategoryDashboardFragmentToCategoryListActivity(categ)

    }
}