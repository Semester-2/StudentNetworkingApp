package com.app.student.networking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.student.networking.model.AnnoucementData
import com.app.student.networking.model.ResponseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class EnrollmentViewModel : ViewModel(),ChildEventListener {

    var enrollmentLiveData: MutableLiveData<List<ResponseData>> = MutableLiveData()

    init{
        fetchAnnouncementsFromDb()
    }

    private fun fetchAnnouncementsFromDb() {
        val db = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val activityList = db.getReference("users").child(user.uid).child("activities")

        activityList.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ${dataSnapshot.childrenCount}")
                val list : ArrayList<String> = ArrayList<String>()
                for (valueRes in dataSnapshot.children) {
                    valueRes.key?.let { Log.d(TAG, it) }
                    valueRes.key?.let { list.add(it) }
                }
                if(list.size > 0)
                    matchActivities(list)
                else{
                    enrollmentLiveData.postValue(null)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "onDataChange: ${databaseError.code}")
            }
        })
    }

    fun matchActivities(activityList : ArrayList<String>){
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("/announcements")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ${dataSnapshot.childrenCount}")
                val list : ArrayList<ResponseData> = ArrayList<ResponseData>()
                for (valueRes in dataSnapshot.children) {
                    val key = valueRes.key
                    if(activityList.contains(key)) {
                        val data = valueRes.getValue(AnnoucementData::class.java)
                        if (key != null && data != null) {
                            list.add(ResponseData(key, data))
                        }
                    }
                }
                enrollmentLiveData.postValue(list)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "onDataChange: ${databaseError.code}")
            }
        })
    }

    fun deleteEnrolledActivity(id :String ){
        val db = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        val ref = db.getReference("users").child(user.uid).child("activities").child(id)
        val ref1 = db.getReference("announcements").child(id).child("enrollments").child(user.displayName)
        ref.removeValue()
        ref1.removeValue()

        ref.addChildEventListener(this)
    }

    companion object{
        private const val TAG = "EnrollmentViewModel"
    }

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d(TAG, "onChildRemoved")
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
    }

    override fun onCancelled(error: DatabaseError) {
    }
}