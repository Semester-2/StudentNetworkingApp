package com.app.student.networking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.student.networking.model.AnnoucementData
import com.app.student.networking.model.ResponseData
import com.app.student.networking.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class DashboardViewModel : ViewModel() {

    var announcementLiveData: MutableLiveData<List<ResponseData>> = MutableLiveData()

    init{

        fetchAnnouncementsFromDb()
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

    fun fetchToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    var token = task.result
                    Log.d("FirebaseMessaging", "Firebase Token: $token")
                    if (token != null) {
                        updateTokenOnServer(token)
                    }
                }
            })
    }

    fun updateTokenOnServer(token: String){
        val fbUser = FirebaseAuth.getInstance().currentUser

        val db = Firebase.database.reference
        var user = User(fbUser.displayName,fbUser.email,fbUser.photoUrl.toString(),fbUser.phoneNumber,token)

        db.child("users").child(fbUser.uid).setValue(user)

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "updateTokenOnServer: ${dataSnapshot.childrenCount}")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "updateTokenOnServer: ${databaseError.code}")
            }
        })
    }

    companion object{
        private const val TAG = "DashboardViewModel"
    }
}