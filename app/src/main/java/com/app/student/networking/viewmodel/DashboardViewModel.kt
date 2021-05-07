package com.app.student.networking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.student.networking.model.AnnoucementData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging


class DashboardViewModel : ViewModel() {

    var announcementLiveData: MutableLiveData<List<AnnoucementData>> = MutableLiveData()

    init{
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

        fetchAnnouncementsFromDb()
    }



    private fun fetchAnnouncementsFromDb() {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("/announcements")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ${dataSnapshot.childrenCount}")

                val list : ArrayList<AnnoucementData> = ArrayList<AnnoucementData>()

                for (valueRes in dataSnapshot.children) {
                    val data = valueRes.getValue(AnnoucementData::class.java)
                    if (data != null) {
                        list.add(data)
                    }
                }

//                val list: List<AnnoucementData>
//                val t: GenericTypeIndicator<List<AnnoucementData?>?> =
//                    object : GenericTypeIndicator<List<AnnoucementData?>?>() {}
//
//                list = dataSnapshot.getValue(t) as List<AnnoucementData>

                announcementLiveData.postValue(list)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "onDataChange: ${databaseError.code}")
            }
        })
    }

    fun updateTokenOnServer(token: String){
        //send token to server(update user table)
    }

    companion object{
        private const val TAG = "DashboardViewModel"
    }
}