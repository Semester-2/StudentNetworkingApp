package com.app.student.networking.utility

import android.content.Context
import androidx.appcompat.app.AlertDialog

class MyAlertDialog {
    lateinit var alert : AlertDialog

    public fun showAlertDialog(context: Context, title: String, msg : String){
        val dialogBuilder = context?.let { AlertDialog.Builder(it) }
        dialogBuilder?.setMessage(msg)
        alert = dialogBuilder?.create()!!
        alert.setTitle(title)
        alert.setMessage(msg)
        alert.show()
    }

    public fun dismissAlertDialog(){
        if(alert.isShowing){
            alert.dismiss()
        }
    }
}