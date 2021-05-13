package com.app.student.networking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.student.networking.viewmodel.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.loginBtn).setOnClickListener(this)
        findViewById<Button>(R.id.wc_login_btn).setOnClickListener{loginUser()}

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginViewModel.authenticationState.observe(this, Observer { authenticationState ->
            when(authenticationState){
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.d(TAG, "AUTHENTICATED")
                    loginViewModel.checkIfUserExists()
                }
                else -> {
                    Log.d(TAG, "UNAUTHENTICATED")
                }
            }
        })

        loginViewModel.response.observe(this, Observer { response ->
            if(response){
                val intent = Intent(this, MainActivity::class.java )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        })
    }

    override fun onClick(view: View?) {
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        providers
                ).build(), Companion.RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG,"Successfully signed in user" + "${FirebaseAuth.getInstance().currentUser?.displayName}")
            }else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun loginUser() {
        mAuth = FirebaseAuth.getInstance()
        var email: String = email_login.text.toString()
        var password: String = password_login.text.toString()
        if (email == "")
        {
            Toast.makeText(this@LoginActivity, "Please enter your Email ID.", Toast.LENGTH_LONG).show()
        }
        else if (password == "")
        {
            Toast.makeText(this@LoginActivity, "Please enter your Password.", Toast.LENGTH_LONG).show()
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email, password).
            addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this@LoginActivity, "Error occured:" + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object{
        var RC_SIGN_IN : Int = 100
        var TAG = "LoginActivity"
    }
}