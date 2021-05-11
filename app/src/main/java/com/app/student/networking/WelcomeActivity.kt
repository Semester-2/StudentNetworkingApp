package com.app.student.networking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        findViewById<Button>(R.id.wc_login_btn).setOnClickListener{
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.wc_register_btn).setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

}