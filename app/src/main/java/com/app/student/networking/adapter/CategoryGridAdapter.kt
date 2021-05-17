package com.app.student.networking.adapter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.student.networking.CategoryCallback
import com.app.student.networking.R
import com.app.student.networking.fragments.CategoryDashboardFragmentDirections
import com.app.student.networking.fragments.DashboardFragment
import com.app.student.networking.model.CategoryGridData
import androidx.fragment.app.FragmentTransaction as FragmentTransaction1

const val CATEGORY_SELECTED = "CATEGORY_SELECTED"

class CategoryGridAdapter(var list : ArrayList<CategoryGridData> , var listener : CategoryCallback) :
        RecyclerView.Adapter<CategoryGridAdapter.CardHolder>(){

    class CardHolder(private val view: View): RecyclerView.ViewHolder(view){
        val categoryText : TextView = view.findViewById(R.id.categoryNameTV)
        private val categoryImage : ImageView = view.findViewById(R.id.categoryImage)
        val context = view.context
        fun bind(oneItem: CategoryGridData, listener : CategoryCallback) {
            categoryText.text = oneItem.text
            categoryImage.setImageResource(oneItem.image)

            categoryImage.setOnClickListener {
                var action = CategoryDashboardFragmentDirections.actionCategoryDashboardFragmentToCategoryListActivity(oneItem.text)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.category_grid_item, viewGroup, false)

        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(list[position] , listener)
    }
}