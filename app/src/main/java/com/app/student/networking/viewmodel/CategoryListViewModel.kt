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

class CategoryListViewModel : ViewModel() {

    var announcementLiveData: MutableLiveData<List<ResponseData>> = MutableLiveData()
    var user = FirebaseAuth.getInstance().currentUser


     fun fetchAnnouncementsFromDb(category : String) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("/announcements")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ${dataSnapshot.childrenCount}")

                val list : ArrayList<ResponseData> = ArrayList<ResponseData>()

                for (valueRes in dataSnapshot.children) {
                    val key = valueRes.key
                    val data = valueRes.getValue(AnnoucementData::class.java)
                    if (key != null && data != null && data.category == category) {
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