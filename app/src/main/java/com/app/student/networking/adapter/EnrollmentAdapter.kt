package com.app.student.networking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.student.networking.R
import com.app.student.networking.model.ResponseData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EnrollmentAdapter : RecyclerView.Adapter<EnrollmentAdapter.CardHolder>(){

    private var list:List<ResponseData> = ArrayList<ResponseData>()

    class CardHolder(private val view: View): RecyclerView.ViewHolder(view){
        val newsText : TextView = view.findViewById(R.id.headingTV)
        val dateText : TextView = view.findViewById(R.id.dateTV)
        val locText : TextView = view.findViewById(R.id.locTV)
        val registerBtn : TextView = view.findViewById(R.id.regTV)

        private val card : CardView = view.findViewById(R.id.card)
        val context = view.context
        fun bind(oneItem: ResponseData) {
            var list = oneItem.announcements
            newsText.text = list.topic
            dateText.text = list.dateTime?.let { convertToDate(it) }
            //locText.text = list.location
        }
        fun convertToDate(millis: Long): String? {
            val simple: DateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z")
            val result = Date(millis)
            return simple.format(result)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.enrollment_list_view_item, viewGroup, false)

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