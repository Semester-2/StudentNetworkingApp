package com.app.student.networking.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.app.student.networking.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class LoginViewModel : ViewModel() {
    var response : MutableLiveData<Boolean> = MutableLiveData()

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

    fun fetchToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    var token = task.result
                    Log.d("FirebaseMessaging", "Firebase Token: $token")
                    if (token != null) {
                        updateUserOnServer(token)
                    }
                }
            })
    }

    fun updateUserOnServer(token : String){
        val fbUser = FirebaseAuth.getInstance().currentUser

        val db = Firebase.database.reference

        var url : String=""
        if(fbUser.photoUrl != null) {
            url = fbUser.photoUrl.toString()
        }


        var user = User(fbUser.displayName,fbUser.email,url,fbUser.phoneNumber,token)

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