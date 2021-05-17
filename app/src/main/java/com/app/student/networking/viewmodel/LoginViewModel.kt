package com.app.student.networking.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.app.student.networking.MainActivity
import com.app.student.networking.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    var response : MutableLiveData<Boolean> = MutableLiveData()
    var db = Firebase.database.reference
    var user = FirebaseAuth.getInstance().currentUser

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        }else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun checkIfUserExists(){
        val fbUser = FirebaseAuth.getInstance().currentUser
        val db = Firebase.database.reference

        db.child("users").child(fbUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    updateUserOnUsers()
                    updateUserOnServer()
                } else{
                    response.postValue(true)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                response.postValue(false)
            }
        })
    }

    fun updateUserOnServer(){
        val fbUser = FirebaseAuth.getInstance().currentUser
        val db = Firebase.database.reference
        var url : String=""
        if(fbUser.photoUrl != null) {
            url = fbUser.photoUrl.toString()
        }
        var user = User(fbUser.displayName, fbUser.email, url, fbUser.phoneNumber)
        db.child("users").child(fbUser.uid).setValue(user)
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("LoginViewModel", "updateUserOnServer: ${dataSnapshot.childrenCount}")
                response.postValue(true)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("LoginViewModel", "updateUserOnServer: ${databaseError.code}")
                response.postValue(false)
            }
        })
    }

    fun updateUserOnUsers(){
        val fbUser = FirebaseAuth.getInstance().currentUser
        val db = Firebase.database.reference
        var url : String=""
        if(fbUser.photoUrl != null) {
            url = fbUser.photoUrl.toString()
        }
        var refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(fbUser.uid)

        val userHashMap = HashMap<String, Any>()
        userHashMap["uid"] = fbUser.uid
        userHashMap["username"] = fbUser.displayName
        userHashMap["profile"] =url
        userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/studentnetworking-1edd4.appspot.com/o/sjsu.jpeg?alt=media&token=5a003946-a0e4-4c88-bb8d-9b30eb1f3c94"
        userHashMap["status"] = "offline"
        userHashMap["search"] = fbUser.displayName.toLowerCase()
        userHashMap["facebook"] = "https://m.facebook.com"
        userHashMap["instagram"] = "https://m.instagram.com"
        userHashMap["website"] = "https://www.google.com"
        refUsers.updateChildren(userHashMap)

    }

    fun getUserInfo(): String {
        val currentuser = FirebaseAuth.getInstance().currentUser//FirebaseUserLiveData().firebaseAuth.currentUser
        val name = currentuser?.displayName
        val email = currentuser?.email
        val photoUrl = currentuser?.photoUrl

        val emailVerified = currentuser?.isEmailVerified

        val uid = currentuser?.uid

        val userinfo = "Username: ${name} \n ${email} "

        Log.i(
                "MainActivity",
                "LoginViewModel User info, name: ${name}; email: ${email} ${emailVerified}; photoUrl: ${photoUrl}; uid:${uid} ."
        )
        return userinfo
    }
}