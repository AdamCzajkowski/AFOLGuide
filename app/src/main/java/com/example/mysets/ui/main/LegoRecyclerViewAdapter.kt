package com.example.mysets.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.databinding.SingleLegoSetBinding
import com.example.mysets.extension.inflate
import com.example.mysets.models.LegoSet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.single_lego_set.view.*

class LegoRecyclerViewAdapter: RecyclerView.Adapter<LegoRecyclerViewAdapter.ViewHolder>() {

    private val listOfLegoSet = mutableListOf<LegoSet>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        //val view = viewGroup.inflate().inflate(R.layout.single_lego_set, viewGroup, false)
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = SingleLegoSetBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfLegoSet[position])
    }

    override fun getItemCount(): Int {
        return listOfLegoSet.size
    }

    fun swapList(list: MutableList<LegoSet>) {
        listOfLegoSet.clear()
        listOfLegoSet.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SingleLegoSetBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: LegoSet) {
            binding.legoSet = item
            binding.executePendingBindings()
        }
    }
}