package com.app.student.networking

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        findViewById<ImageView>(R.id.splashImage).startAnimation(slideAnimation)

        Handler().postDelayed({
            var mAuth =  FirebaseAuth.getInstance()
            val user = mAuth.currentUser
            if(user != null){
                val intent = Intent(this, MainActivity::class.java )
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }
}