package com.app.student.networking.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.student.networking.AnnouncementScrollingActivity
import com.app.student.networking.R
import com.app.student.networking.SELECTED_ANNOUNCEMENT_DATA
import com.app.student.networking.model.AnnoucementData
import com.app.student.networking.model.ResponseData
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.Serializable

class AnnouncementListAdapter  :
    RecyclerView.Adapter<AnnouncementListAdapter.CardHolder>(){

    private var list:List<ResponseData> = ArrayList<ResponseData>()

    class CardHolder(private val view: View): RecyclerView.ViewHolder(view){
        val newsText : TextView = view.findViewById(R.id.headingTV)
        private val newsImage : ImageView = view.findViewById(R.id.headingIV)
        private val card : CardView = view.findViewById(R.id.card)
        val context = view.context
        fun bind(oneItem: ResponseData) {
            var list = oneItem.announcements
            newsText.text = list.topic
            val url = list.image

            Glide
                .with(context)
                .load(url)
                .centerCrop()
                .into(newsImage)

            card.setOnClickListener {

                val intent = Intent(context, AnnouncementScrollingActivity::class.java).apply {
                    putExtra(SELECTED_ANNOUNCEMENT_DATA, oneItem as Serializable)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.announcement_item_view, viewGroup, false)

        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(list: List<ResponseData>){
        this.list = list
    }
}