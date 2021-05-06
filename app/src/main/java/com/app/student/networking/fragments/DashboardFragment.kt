package com.app.student.networking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.student.networking.R
import com.app.student.networking.databinding.FragmentDashboardBinding
import com.app.student.networking.viewmodel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment() {

    lateinit var binding : FragmentDashboardBinding
    lateinit var viewModel : DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        val currentUser = FirebaseAuth.getInstance().currentUser
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = String.format(getString(R.string.welcome_user), currentUser.displayName)

        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        binding.welcomeTV.text = String.format(getString(R.string.welcome_user),"Gunjan")

        return binding.root
    }
}