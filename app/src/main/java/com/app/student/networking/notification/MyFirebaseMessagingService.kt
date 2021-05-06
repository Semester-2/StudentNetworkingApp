package com.app.student.networking.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "Notification received: "+ message.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object{
        private const val TAG = "FirebaseMessaging"
    }
}