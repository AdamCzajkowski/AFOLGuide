package com.example.mysets.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleLegoSetBinding
import com.example.mysets.models.LegoSet

class LegoRecyclerViewAdapter: RecyclerView.Adapter<LegoRecyclerViewAdapter.ViewHolder>() {

    private val listOfLegoSet = mutableListOf<LegoSet>()

    var selectedItem: ((LegoSet) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = SingleLegoSetBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfLegoSet[position])

        holder.itemView.setOnClickListener {
            selectedItem?.invoke(listOfLegoSet[position])
        }
    }

    override fun getItemCount(): Int {
        return listOfLegoSet.size
    }

    fun clearList() {
        listOfLegoSet.clear()
        notifyDataSetChanged()
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