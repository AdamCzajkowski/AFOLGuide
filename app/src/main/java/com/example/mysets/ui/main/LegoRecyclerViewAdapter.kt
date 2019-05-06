package com.example.mysets.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.extension.inflate
import com.example.mysets.models.LegoSet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.single_lego_set.view.*

class LegoRecyclerViewAdapter(): RecyclerView.Adapter<LegoRecyclerViewAdapter.ViewHolder>() {

    private val listOfLegoSet = mutableListOf<LegoSet>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = viewGroup.inflate().inflate(R.layout.single_lego_set, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fillData(listOfLegoSet[position])
    }

    override fun getItemCount(): Int {
        return listOfLegoSet.size
    }

    fun swapList(list: MutableList<LegoSet>) {
        listOfLegoSet.clear()
        listOfLegoSet.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun fillData(item: LegoSet) {
            itemView.number_set_value_text_id.text = item.set_num
            itemView.name_set_text_value_id.text = item.name
            itemView.release_date_text_value_id.text = item.year.toString()
            itemView.number_parts_text_value_id.text = item.num_parts.toString()
            Picasso.get().load(item.set_img_url).into(itemView.lego_image_id)
        }
    }
}