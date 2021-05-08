package com.app.student.networking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.student.networking.viewmodel.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.loginBtn).setOnClickListener(this)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginViewModel.authenticationState.observe(this, Observer { authenticationState ->
            when(authenticationState){
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.d(TAG, "AUTHENTICATED")
                    loginViewModel.fetchToken()

                }
                else -> {
                    Log.d(TAG, "UNAUTHENTICATED")
                }
            }
        })

        loginViewModel.response.observe(this, Observer { response ->
            if(response){
                val intent = Intent(this, MainActivity::class.java )
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

    companion object{
        var RC_SIGN_IN : Int = 100
        var TAG = "LoginActivity"
    }
}