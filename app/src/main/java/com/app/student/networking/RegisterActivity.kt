package com.app.student.networking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        var username: String = username_register.text.toString()
        var email: String = email_register.text.toString()
        var password: String = password_register.text.toString()

        if (username == "") {
            Toast.makeText(this@RegisterActivity, "Please enter your Username.", Toast.LENGTH_LONG).show()
        } else if (email == "") {
            Toast.makeText(this@RegisterActivity, "Please enter your Email ID.", Toast.LENGTH_LONG).show()

        } else if (password == "") {
            Toast.makeText(this@RegisterActivity, "Please enter your Password.", Toast.LENGTH_LONG).show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username
                    userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/whatsappchatapp-2f6ba.appspot.com/o/profile.png?alt=media&token=615f2512-270c-4525-8e98-5cf0a334f66f"
                    userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/whatsappchatapp-2f6ba.appspot.com/o/option1.jpg?alt=media&token=f955c974-872a-49f9-9656-0e0dddf20f19"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.toLowerCase()
                    userHashMap["facebook"] = "https://m.facebook.com"
                    userHashMap["instagram"] = "https://m.instagram.com"
                    userHashMap["website"] = "https://www.google.com"

                    refUsers.updateChildren(userHashMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                } else {
                    //Non-asserted calls - !! means contains a message
                    Toast.makeText(this@RegisterActivity, "Error occured:" + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }

        }
    }
}