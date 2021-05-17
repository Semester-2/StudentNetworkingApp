package com.app.student.networking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.student.networking.MyCallback
import com.app.student.networking.R
import com.app.student.networking.fragments.EnrollmentFragment
import com.app.student.networking.model.ResponseData
import com.app.student.networking.viewmodel.EnrollmentViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EnrollmentAdapter(var listener : MyCallback) : RecyclerView.Adapter<EnrollmentAdapter.CardHolder>(){

    private var list:List<ResponseData> = ArrayList<ResponseData>()

    class CardHolder(private val view: View): RecyclerView.ViewHolder(view){
        val newsText : TextView = view.findViewById(R.id.headingTV)
        val dateText : TextView = view.findViewById(R.id.dateTV)
        val locText : TextView = view.findViewById(R.id.locTV)
        val registerBtn : TextView = view.findViewById(R.id.regTV)

        private val card : CardView = view.findViewById(R.id.card)
        val context = view.context
        fun bind(oneItem: ResponseData, listener: MyCallback) {
            var list = oneItem.announcements
            newsText.text = list.topic
            dateText.text = list.dateTime?.let { convertToDate(it) }
            registerBtn.setOnClickListener{
                oneItem.key?.let { it1 -> listener.onClickUnregister(it1) }
            }
            locText.text = list.location
        }
        fun convertToDate(millis: Long): String? {
            val simple: DateFormat = SimpleDateFormat("dd MMM yyyy")
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
        if(list != null)
            return list.size
        else return 0
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(list[position],listener)
    }

    fun updateList(list: List<ResponseData>){
        this.list = list
    }
}