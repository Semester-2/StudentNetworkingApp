package com.app.student.networking.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.student.networking.R
import com.app.student.networking.databinding.FragmentPostBinding
import com.app.student.networking.model.AnnoucementData
import com.app.student.networking.utility.MyAlertDialog
import com.app.student.networking.viewmodel.PostViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.util.*

const val IMAGE_REQUEST_CODE = 33
class PostFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentPostBinding
    lateinit var viewModel : PostViewModel
    //var alertDialog = MyAlertDialog()
    lateinit var calendar : Calendar
    lateinit var myImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false)
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        binding.postBtn.setOnClickListener(this)
        binding.calendarIV.setOnClickListener{showStartDatePicker()}
        myImage = binding.uploadImage
        myImage.setOnClickListener{changeImage()}

        viewModel.response.observe(viewLifecycleOwner, Observer {
           // alertDialog.dismissAlertDialog()
            binding.titleEt.text.clear()
            binding.despET.text.clear()
            binding.dateTimeET.text.clear()
            binding.locationET.text.clear()
        })
        return binding.root
    }

    private fun changeImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            hideKeybaord(view)
           // alertDialog.showAlertDialog(requireActivity(), "Posting Announcement", "Please wait...")
            var user = FirebaseAuth.getInstance().currentUser
            val title = binding.titleEt.text.toString()
            val desc = binding.despET.text.toString()
            val dateTime = binding.dateTimeET.text.toString()
            val location = binding.locationET.text.toString()
            val url = "https://firebasestorage.googleapis.com/v0/b/studentnetworking-1edd4.appspot.com/o/btf_detail.jpg?alt=media&token=4dd8439d-e4ea-4606-8e39-ebc15af49a47"
            val data = AnnoucementData(
                    title,
                    desc,
                    viewModel.time,
                    viewModel.uri.toString(),
                    location,
                    user.displayName
            )
            viewModel.postAnnouncement(data)
        }
    }

    private fun hideKeybaord(v: View) {
        val inputMethodManager: InputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    private fun showStartDatePicker() {
        calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val month: Int = calendar.get(Calendar.MONTH)
        val year: Int = calendar.get(Calendar.YEAR)

        val picker = DatePickerDialog(
            requireActivity(),
            { view, year, month, dayOfMonth ->
                val selectedDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                binding.dateTimeET.setText(selectedDate)
                val millis: Long = convertDateToTimeInMillis(year, month + 1, dayOfMonth)
                viewModel.time = millis
            }, year, month, day
        )
        picker.show()
    }

    private fun convertDateToTimeInMillis(year: Int, month: Int, dayOfMonth: Int): Long {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = dayOfMonth
        cal[Calendar.MONTH] = month - 1
        cal[Calendar.YEAR] = year
        return cal.timeInMillis
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode != IMAGE_REQUEST_CODE)
        {
            Toast.makeText(requireActivity(), "Activity result received", Toast.LENGTH_LONG).show()
        }

        val uri = data?.data
        val file = File(uri!!.path)
        val name = file.name

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            myImage.setImageURI(data?.data)
            viewModel.uploadImageOnStorage(myImage,name)
        }
    }
}