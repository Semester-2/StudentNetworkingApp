package com.app.student.networking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.student.networking.model.AnnoucementData
import com.app.student.networking.model.ResponseData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EnrollmentViewModel : ViewModel() {

    var enrollmentLiveData: MutableLiveData<List<ResponseData>> = MutableLiveData()

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

                enrollmentLiveData.postValue(list)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "onDataChange: ${databaseError.code}")
            }
        })
    }

    companion object{
        private const val TAG = "EnrollmentViewModel"
    }
}