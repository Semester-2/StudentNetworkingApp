package com.app.student.networking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.student.networking.model.ResponseData
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val SELECTED_ANNOUNCEMENT_DATA = "Selected Announcement"
class AnnouncementScrollingActivity : AppCompatActivity() {
    lateinit var key: String
    lateinit var registerBtn: Button
    private lateinit var dataItem: ResponseData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement_scrolling)

        val titleTV = findViewById<TextView>(R.id.titleTV)
        val dateTimeTV = findViewById<TextView>(R.id.dateTimeTV)
        val descriptionTV = findViewById<TextView>(R.id.descriptionTV)
        val locationTV = findViewById<TextView>(R.id.locationTV)
        val publishedByTV = findViewById<TextView>(R.id.publishedBYTV)
        val chatBtn = findViewById<ImageView>(R.id.chatImageBtn)
        registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener { registerForEvent() }
        chatBtn.setOnClickListener{launchChat()}

        val collapsingImageView = findViewById<ImageView>(R.id.collapsingToolbarHeaderImage)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { handleFab() }
        val toolbarLayout: CollapsingToolbarLayout =
                findViewById<CollapsingToolbarLayout>(R.id.toolbarLayout)

        dataItem = (intent.extras?.get(SELECTED_ANNOUNCEMENT_DATA) as? ResponseData)!!
        if (dataItem != null) {
            var user = FirebaseAuth.getInstance().currentUser
            var list = dataItem.announcements
            titleTV.text = list.topic
            dateTimeTV.text = list.dateTime?.let { convertToDate(it) }
            descriptionTV.text = list.description
            locationTV.text = list.location
            publishedByTV.text = list.publishedBy

            var map = list.enrollments
            if (map != null) {
                for ((k, v) in map) {
                    println("$k = $v")
                    if(k == user.displayName && v){
                        registerBtn.text = "Unregister"
                    }
                }
            }

            key = dataItem.key.toString()
            val url = list.image

            Glide
                    .with(this)
                    .load(url)
                    .centerCrop()
                    .into(collapsingImageView);
            toolbarLayout.title = list.category
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun launchChat(){
        val intent = Intent(this, ChatMainActivity::class.java)
        startActivity(intent)
    }

    fun registerForEvent() {
        val db = Firebase.database.reference
        val user = FirebaseAuth.getInstance().currentUser
        lateinit var task: Task<Void>
        if (registerBtn.text == "Register") {
            task = db.child("users").child(user.uid).child("activities").child(key).setValue(true)
            task.addOnSuccessListener {
                registerBtn.text = "Unregister"
                Log.d(TAG, "addOnSuccessListener: $it")
                announcementRegister()
            }
            task.addOnFailureListener {
                Log.d(TAG, "addOnFailureListener")
            }

        } else if (registerBtn.text == "Unregister") {

            val ref = FirebaseDatabase.getInstance().getReference("users").child(user.uid).child("activities").child(key)
            ref.removeValue()
            val ref1 = FirebaseDatabase.getInstance().getReference("announcements").child(key).child("enrollments").child(user.displayName)
            ref1.removeValue()
            registerBtn.text = "Register"
        }
    }

    fun announcementRegister(){
        val db = Firebase.database.reference
        val user = FirebaseAuth.getInstance().currentUser
        dataItem.key?.let { db.child("announcements").child(it).child("enrollments").child(user.displayName).setValue(true) }
    }

    companion object{
        private const val TAG = "AnnouncementScroll"
    }

   private fun handleFab() {
       val announcement = dataItem.announcements
       val data = "Description: " + announcement.description + " Location: " + announcement.location + " Time: "+ announcement.dateTime
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun convertToDate(millis: Long): String? {
        val simple: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val result = Date(millis)
        return simple.format(result)
    }
}
