package com.app.student.networking

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.app.student.networking.model.AnnoucementData
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val SELECTED_ANNOUNCEMENT_DATA = "Selected Announcement"
class AnnouncementScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_announcement_scrolling)

        val titleTV = findViewById<TextView>(R.id.titleTV)
        val dateTimeTV = findViewById<TextView>(R.id.dateTimeTV)
        val descriptionTV = findViewById<TextView>(R.id.descriptionTV)
        val locationTV = findViewById<TextView>(R.id.locationTV)

        val collapsingImageView = findViewById<ImageView>(R.id.collapsingToolbarHeaderImage)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        //fab.setOnClickListener(this)

        val toolbarLayout: CollapsingToolbarLayout =
            findViewById<CollapsingToolbarLayout>(R.id.toolbarLayout)

        val dataItem: AnnoucementData? = intent.extras?.get(SELECTED_ANNOUNCEMENT_DATA) as? AnnoucementData
        if (dataItem != null) {
            titleTV.text = dataItem.topic
            dateTimeTV.text = dataItem.dateTime?.let { convertToDate(it) }
            descriptionTV.text = dataItem.description
            //locationTV.text = dataItem.location
            locationTV.text = "Town hall"
            val url = dataItem.image

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
