package com.application.afol.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.databinding.SingleFavoriteLegoSetBinding
import com.application.afol.databinding.SingleLegoSetBinding
import com.application.afol.models.LegoSet
import com.application.afol.utility.autoNotify
import kotlin.properties.Delegates

class MySetsRecyclerViewAdapter: RecyclerView.Adapter<MySetsRecyclerViewAdapter.ViewHolder>() {

    var listOfLegoSet: MutableList<LegoSet> by Delegates.observable(mutableListOf()) { _, oldList, newList ->
        autoNotify(oldList, newList) { old, new -> old.id == new.id }
    }

    var mRecentlyDeletedItemPosition = 0

    lateinit var mRecentlyDeletedItem: LegoSet

    var selectedItem: ((LegoSet) -> Unit)? = null

    var swipedItem: ((LegoSet) -> Unit)? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context.applicationContext)
        val binding = SingleFavoriteLegoSetBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(listOfLegoSet[position])

        holder.itemView.setOnClickListener {
            Log.i("rv", position.toString())
            selectedItem?.invoke(listOfLegoSet[position])
        }
    }

    override fun getItemCount(): Int {
        return listOfLegoSet.size
    }

    fun swapItemHorizontally(position: Int) {
        mRecentlyDeletedItem = listOfLegoSet[position]
        swipedItem?.invoke(listOfLegoSet[position])
        mRecentlyDeletedItemPosition = position
    }

    class ViewHolder(private val binding: SingleFavoriteLegoSetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: LegoSet) {
            binding.legoSet = item
            binding.executePendingBindings()
        }
    }
}