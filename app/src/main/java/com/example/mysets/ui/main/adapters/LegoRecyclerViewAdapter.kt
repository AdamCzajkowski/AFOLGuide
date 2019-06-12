package com.example.mysets.ui.main.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleLegoSetBinding
import com.example.mysets.models.LegoSet
import com.example.mysets.utility.autoNotify
import kotlin.properties.Delegates

class LegoRecyclerViewAdapter : RecyclerView.Adapter<LegoRecyclerViewAdapter.ViewHolder>() {

    var listOfLegoSet: MutableList<LegoSet> by Delegates.observable(mutableListOf()) { _, oldList, newList ->
        autoNotify(oldList, newList) { old, new -> old.id == new.id }
    }

    var selectedItem: ((LegoSet) -> Unit)? = null

    var endMarker: ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context.applicationContext)
        val binding = SingleLegoSetBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(listOfLegoSet[position])

        holder.itemView.setOnClickListener {
            selectedItem?.invoke(listOfLegoSet[position])
        }

        if (position == itemCount - (0.1 * itemCount).toInt()) {
            endMarker?.invoke(true)
            Log.i("searchSet", "endMarker true multiplier $itemCount")
        } else {
            endMarker?.invoke(false)
            Log.i("searchSet", "endMarker false multiplier $itemCount")
        }
    }

    override fun getItemCount(): Int {
        return listOfLegoSet.size
    }

    fun clearList() {
        listOfLegoSet.clear()
        notifyDataSetChanged()
    }

    fun addToList(list: MutableList<LegoSet>) {
        listOfLegoSet.addAll(list)
        notifyItemRangeChanged(itemCount - list.size, itemCount)
    }

    class ViewHolder(private val binding: SingleLegoSetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: LegoSet) {
            binding.legoSet = item
            binding.executePendingBindings()
        }
    }
}