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
import com.app.student.networking.MyCallback
import com.app.student.networking.R
import com.app.student.networking.adapter.AnnouncementListAdapter
import com.app.student.networking.adapter.EnrollmentAdapter
import com.app.student.networking.databinding.FragmentDashboardBinding
import com.app.student.networking.utility.MyAlertDialog
import com.app.student.networking.viewmodel.DashboardViewModel
import com.app.student.networking.viewmodel.EnrollmentViewModel
import com.google.firebase.auth.FirebaseAuth

class EnrollmentFragment : Fragment(), MyCallback {

    lateinit var binding : FragmentDashboardBinding
    lateinit var viewModel : EnrollmentViewModel
    lateinit var adapter : EnrollmentAdapter
    var newsAlertDialog = MyAlertDialog()

    companion object{
        const val TAG : String = "EnrollmentFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Enrollments"
        binding.emptyListTV.visibility = View.GONE
        viewModel = ViewModelProvider(this).get(EnrollmentViewModel::class.java)

        activity?.let { newsAlertDialog.showAlertDialog(
            activity as AppCompatActivity,
            "Your Enrollments",
            "Loading... Please wait"
        ) }

        adapter = EnrollmentAdapter(this)
        val recyclerView: RecyclerView = binding.annoucementRV
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        viewModel.enrollmentLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "Headlines Count: $it")
            newsAlertDialog.dismissAlertDialog()
            if(it != null) {
                binding.emptyListTV.visibility = View.GONE
                adapter.updateList(it)
                adapter.notifyDataSetChanged()
            }else{
                binding.emptyListTV.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    override fun onClickUnregister(id : String) {
        Log.d(TAG, "onClickUnregister")
        viewModel.deleteEnrolledActivity(id)
    }
}