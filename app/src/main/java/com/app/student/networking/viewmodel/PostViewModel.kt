package com.app.student.networking.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.student.networking.model.AnnoucementData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class PostViewModel : ViewModel() , ChildEventListener {

    private lateinit var database: DatabaseReference
    var response : MutableLiveData<Boolean> = MutableLiveData()
    var time = 0L
    lateinit var storage : FirebaseStorage
    lateinit var uri : Uri

    fun uploadImageOnStorage(view : ImageView, name: String){
        storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(name)

        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = (view.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)

        uploadTask.addOnFailureListener {
            Log.d(TAG , "Upload failed")

        }.addOnSuccessListener { taskSnapshot ->
            Log.d(TAG , "Upload success: $taskSnapshot")
            storageRef.child(name).downloadUrl.addOnSuccessListener {
                Log.d(TAG , "Upload success: $it")
                uri = it
            }.addOnFailureListener {
                Log.d(TAG , "Upload failed")
            }
        }
    }

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

    companion object{
        private const val TAG = "PostViewModel"
    }
}