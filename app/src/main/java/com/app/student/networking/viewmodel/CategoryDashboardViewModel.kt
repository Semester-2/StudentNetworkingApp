package com.app.student.networking.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.app.student.networking.R
import com.app.student.networking.model.Categories
import com.app.student.networking.model.CategoryGridData
import com.app.student.networking.notification.Token
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class CategoryDashboardViewModel : ViewModel() {

    var list : ArrayList<CategoryGridData> = ArrayList()
    var user = FirebaseAuth.getInstance().currentUser

    init {
        list = initializeList()
        fetchToken()
    }

    fun initializeList() : ArrayList<CategoryGridData>{
        var l : List<CategoryGridData> = ArrayList()
        l = arrayListOf(
                CategoryGridData( R.drawable.academic , Categories.ACADEMICS.category) ,
                CategoryGridData( R.drawable.carrers , Categories.CAREERS.category),
                CategoryGridData( R.drawable.groups , Categories.GROUPS.category),
                CategoryGridData( R.drawable.sports , Categories.SPORTS.category),
                CategoryGridData( R.drawable.international_student , Categories.INTERNATIONAL_STUDENT.category),
                CategoryGridData( R.drawable.fun_activity , Categories.FUN_ACTIVITY.category),
        )
        return l
    }

    fun fetchToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    var token = task.result
                    Log.d("FirebaseMessaging", "Firebase Token: $token")
                    if (token != null) {
                        var t = Token(token!!)
                        FirebaseDatabase.getInstance().reference.child("Tokens").child(user.uid).setValue(t)
                    }
                }
            })
    }
}