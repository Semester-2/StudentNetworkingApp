package com.app.student.networking.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.student.networking.R
import com.app.student.networking.databinding.FragmentPostBinding
import com.app.student.networking.model.AnnoucementData
import com.app.student.networking.utility.MyAlertDialog
import com.app.student.networking.viewmodel.PostViewModel


class PostFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentPostBinding
    lateinit var viewModel : PostViewModel
    var alertDialog = MyAlertDialog()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false)
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        binding.postBtn.setOnClickListener(this)

        viewModel.response.observe(viewLifecycleOwner, Observer {
            alertDialog.dismissAlertDialog()
            binding.titleEt.text.clear()
            binding.despET.text.clear()
            binding.dateTimeET.text.clear()
            binding.locationET.text.clear()
        })

        return binding.root
    }

    override fun onClick(view: View?) {
        if (view != null) {
            hideKeybaord(view)
        }
        alertDialog.showAlertDialog(requireActivity(),"Posting Announcement", "Please wait...")
        val title = binding.titleEt.text.toString()
        val desc = binding.despET.text.toString()
        val dateTime = binding.dateTimeET.text.toString()
        val location = binding.locationET.text.toString()
        val url = "https://firebasestorage.googleapis.com/v0/b/studentnetworking-1edd4.appspot.com/o/btf_detail.jpg?alt=media&token=4dd8439d-e4ea-4606-8e39-ebc15af49a47"
        //val imageurl
        val data = AnnoucementData(title,desc,dateTime,"https://firebasestorage.googleapis.com/v0/b/studentnetworking-1edd4.appspot.com/o/btf_detail.jpg?alt=media&token=4dd8439d-e4ea-4606-8e39-ebc15af49a47")

        viewModel.postAnnouncement(data)
    }

    private fun hideKeybaord(v: View) {
        val inputMethodManager: InputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }
}