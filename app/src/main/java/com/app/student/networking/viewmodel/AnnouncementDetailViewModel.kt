package com.app.student.networking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.student.networking.AnnouncementScrollingActivity
import com.app.student.networking.model.RegisterState
import com.app.student.networking.model.ResponseData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class AnnouncementDetailViewModel: ViewModel() {

    var user: FirebaseUser

    init {
        user = FirebaseAuth.getInstance().currentUser
    }


}