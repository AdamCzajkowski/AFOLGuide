package com.application.afol.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.databinding.SingleLegoSetBinding
import com.application.afol.models.LegoSet

class LegoThemesRecyclerViewAdapter :
    RecyclerView.Adapter<LegoThemesRecyclerViewAdapter.ViewHolder>() {
    private val listOfLegoSetFromTheme = mutableListOf<LegoSet>()

    var endMarker: ((Boolean) -> Unit)? = null

    var selectedItem: ((LegoSet) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SingleLegoSetBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfLegoSetFromTheme.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfLegoSetFromTheme[position])

        if (position == (itemCount - (0.1 * itemCount).toInt())) {
            endMarker?.invoke(true)
            Log.i("themeset", "end marker true ------------------------>")
        } else {
            endMarker?.invoke(false)
            Log.i("themeset", "end marker false")
        }

        holder.itemView.setOnClickListener {
            selectedItem?.invoke(listOfLegoSetFromTheme[position])
        }
    }

    fun clearList() {
        listOfLegoSetFromTheme.clear()
        notifyDataSetChanged()
    }

    fun swapList(list: MutableList<LegoSet>) {
        listOfLegoSetFromTheme.clear()
        listOfLegoSetFromTheme.addAll(list)
        notifyDataSetChanged()
    }

    fun addToList(list: MutableList<LegoSet>) {
        listOfLegoSetFromTheme.addAll(list)
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