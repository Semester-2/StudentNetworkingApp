package com.app.student.networking.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.student.networking.model.AnnoucementData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostViewModel : ViewModel() , ChildEventListener {

    private lateinit var database: DatabaseReference
    var response : MutableLiveData<Boolean> = MutableLiveData()
    var time = 0L

    fun postAnnouncement(data: AnnoucementData){

        database = Firebase.database.reference
        database.addChildEventListener(this)

        database.child("announcements").push().setValue(data)
    }

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        response.postValue(true)
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        response.postValue(true)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        response.postValue(true)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        response.postValue(true)
    }

    override fun onCancelled(error: DatabaseError) {
        response.postValue(false)
    }
}