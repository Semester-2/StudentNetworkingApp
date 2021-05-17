package com.app.student.networking

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.student.networking.adapter.AnnouncementListAdapter
import com.app.student.networking.adapter.CATEGORY_SELECTED
import com.app.student.networking.databinding.FragmentDashboardBinding
import com.app.student.networking.fragments.DashboardFragment
import com.app.student.networking.model.Categories
import com.app.student.networking.viewmodel.CategoryListViewModel

class CategoryListActivity : AppCompatActivity() {

    private lateinit var binding: FragmentDashboardBinding
    lateinit var viewModel : CategoryListViewModel
    lateinit var adapter : AnnouncementListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_dashboard)
        supportActionBar!!.title = "Dashboard"
        viewModel = ViewModelProvider(this).get(CategoryListViewModel::class.java)

//        var selectedCategory = savedInstanceState?.getString(CATEGORY_SELECTED)
//        viewModel.fetchAnnouncementsFromDb(selectedCategory as String)


            val args: CategoryListActivityArgs by navArgs()
            var categorySelected = args.categorySelected
            viewModel.fetchAnnouncementsFromDb(categorySelected)


        adapter = AnnouncementListAdapter()
        val recyclerView: RecyclerView = binding.annoucementRV
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.announcementLiveData.observe(this, Observer {
            Log.d(DashboardFragment.TAG, "Headlines Count: $it")
            adapter.updateList(it)
            adapter.notifyDataSetChanged()
        })
    }
}