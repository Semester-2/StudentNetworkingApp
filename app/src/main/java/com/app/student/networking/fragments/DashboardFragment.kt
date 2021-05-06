package com.app.student.networking.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.student.networking.adapter.AnnouncementListAdapter
import com.app.student.networking.R
import com.app.student.networking.databinding.FragmentDashboardBinding
import com.app.student.networking.utility.MyAlertDialog
import com.app.student.networking.viewmodel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment() {

    lateinit var binding : FragmentDashboardBinding
    lateinit var viewModel : DashboardViewModel
    lateinit var adapter : AnnouncementListAdapter
    var newsAlertDialog = MyAlertDialog()

    companion object{
        const val TAG : String = "TopHeadlinesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        val currentUser = FirebaseAuth.getInstance().currentUser
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = String.format(getString(R.string.welcome_user), currentUser.displayName)

        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        activity?.let { newsAlertDialog.showAlertDialog(
            activity as AppCompatActivity,
            "fetching Announcements",
            "Loading... Please wait"
        ) }

        adapter = AnnouncementListAdapter()
        val recyclerView: RecyclerView = binding.annoucementRV
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        viewModel.announcementLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "Headlines Count: $it")
            newsAlertDialog.dismissAlertDialog()
            adapter.updateList(it)
            adapter.notifyDataSetChanged()
        })

        return binding.root
    }
}