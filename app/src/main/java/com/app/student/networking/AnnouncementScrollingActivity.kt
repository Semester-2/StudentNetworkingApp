package com.app.student.networking

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.student.networking.model.ResponseData
import com.app.student.networking.utility.MyAlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val SELECTED_ANNOUNCEMENT_DATA = "Selected Announcement"
class AnnouncementScrollingActivity : AppCompatActivity() {


    lateinit var key : String
    var alertDialog = MyAlertDialog()
    lateinit var registerBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_announcement_scrolling)

        val titleTV = findViewById<TextView>(R.id.titleTV)
        val dateTimeTV = findViewById<TextView>(R.id.dateTimeTV)
        val descriptionTV = findViewById<TextView>(R.id.descriptionTV)
        val locationTV = findViewById<TextView>(R.id.locationTV)
        registerBtn = findViewById<Button>(R.id.registerBtn)
        registerBtn.setOnClickListener{registerForEvent()}

        val collapsingImageView = findViewById<ImageView>(R.id.collapsingToolbarHeaderImage)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        //fab.setOnClickListener(this)

        val toolbarLayout: CollapsingToolbarLayout =
            findViewById<CollapsingToolbarLayout>(R.id.toolbarLayout)

        val dataItem: ResponseData? = intent.extras?.get(SELECTED_ANNOUNCEMENT_DATA) as? ResponseData
        if (dataItem != null) {
            var list = dataItem.announcements
            titleTV.text = list.topic
            dateTimeTV.text = list.dateTime?.let { convertToDate(it) }
            descriptionTV.text = list.description
            locationTV.text = list.location

            key = dataItem.key.toString()
            val url = list.image

            Glide
                .with(this)
                .load(url)
                .centerCrop()
                .into(collapsingImageView);
            toolbarLayout.title = "News Detail"
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun convertToDate(millis: Long): String? {
        val simple: DateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z")
        val result = Date(millis)
        return simple.format(result)
    }

    fun registerForEvent(){
        val db = Firebase.database.reference
        val user = FirebaseAuth.getInstance().currentUser
        lateinit var task: Task<Void>
        if(registerBtn.text == "Register"){
            alertDialog.showAlertDialog(this, "Registering", "Please wait...")
            val map: MutableMap<String, Any> = HashMap()
            map[key] = "true"
            task = db.child("users").child(user.uid).child("activities").child(key).setValue(true)
            task.addOnSuccessListener {
                alertDialog.dismissAlertDialog()
                registerBtn.text = "Unregister"
                Log.d(TAG, "addOnSuccessListener: $it")
            }
            task.addOnFailureListener{
                alertDialog.dismissAlertDialog()
                Log.d(TAG, "addOnFailureListener")
            }
        }
        else if(registerBtn.text == "Unregister"){
            alertDialog.showAlertDialog(this, "Unregistering", "Please wait...")

            val map: MutableMap<String, Any> = HashMap()
            map[key] = "false"
            task = db.child("users").child(user.uid).child("activities").updateChildren(map)
            task.addOnSuccessListener {
                alertDialog.dismissAlertDialog()
                registerBtn.text = "Register"
                Log.d(TAG, "addOnSuccessListener: $it")
            }
            task.addOnFailureListener{
                alertDialog.dismissAlertDialog()
                Log.d(TAG, "addOnFailureListener")
            }
        }
    }

    companion object{
        private const val TAG = "AnnouncementScroll"
    }


//    override fun onClick(view: View?) {
//        val sendIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, url)
//            type = "text/plain"
//        }
//
//        val shareIntent = Intent.createChooser(sendIntent, null)
//        startActivity(shareIntent)
//    }
}
