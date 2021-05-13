package com.app.student.networking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.student.networking.model.AnnoucementData
import com.app.student.networking.model.ResponseData
import com.app.student.networking.notification.Token
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging

class DashboardViewModel : ViewModel() {

    var announcementLiveData: MutableLiveData<List<ResponseData>> = MutableLiveData()
    var user = FirebaseAuth.getInstance().currentUser

    init{
        fetchAnnouncementsFromDb()
        fetchToken()
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

    private fun fetchAnnouncementsFromDb() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("/announcements")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ${dataSnapshot.childrenCount}")

                val list : ArrayList<ResponseData> = ArrayList<ResponseData>()

                for (valueRes in dataSnapshot.children) {
                    val key = valueRes.key
                    val data = valueRes.getValue(AnnoucementData::class.java)
                    if (key != null && data != null) {
                        list.add(ResponseData(key,data))
                    }
                }

                announcementLiveData.postValue(list)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "onDataChange: ${databaseError.code}")
            }
        })
    }

    companion object{
        private const val TAG = "DashboardViewModel"
    }
}