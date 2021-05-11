package com.app.student.networking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.app.student.networking.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.nav_header.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        navController = Navigation.findNavController(this, R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)

        NavigationUI.setupWithNavController(binding.navView, navController)
        val user = FirebaseAuth.getInstance().currentUser
        headerView = binding.navView.getHeaderView(0)
        headerView.nameTv.text = user.displayName
        headerView.emailTV.text = user.email
        var avatar = headerView.avatar
        Glide
            .with(this)
            .load(user.photoUrl)
            .centerCrop()
            .into(avatar)

        headerView.chatBtn.setOnClickListener{handleChat()}
    }

    fun handleChat(){
        var intent = Intent(this, ChatMainActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.signOutItem){
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                    Log.i(TAG, "Sign out successful")
                    val intent = Intent(this,WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                finish()
                }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object{
        private const val TAG = "MainActivity"
    }
}