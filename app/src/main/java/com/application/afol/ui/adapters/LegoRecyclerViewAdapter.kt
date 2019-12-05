package com.application.afol.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.databinding.SingleLegoSetBinding
import com.application.afol.models.LegoSet
import kotlinx.android.synthetic.main.single_lego_set.view.*

class LegoRecyclerViewAdapter : RecyclerView.Adapter<LegoRecyclerViewAdapter.ViewHolder>() {

    lateinit var mRecentlySwipedItem: LegoSet

    var listOfLegoSet = mutableListOf<LegoSet>()

    var listOfFavoritesLegoSet: MutableList<LegoSet>? = mutableListOf<LegoSet>()

    var selectedItem: ((LegoSet) -> Unit)? = null

    var endMarker: ((Boolean) -> Unit)? = null

    var swipedItem: ((LegoSet) -> Unit)? = null

    var favoriteIconItem: ((LegoSet) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context.applicationContext)
        val binding = SingleLegoSetBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindView(listOfLegoSet[position], listOfFavoritesLegoSet)

        holder.itemView.setOnClickListener {
            selectedItem?.invoke(listOfLegoSet[position])
        }

        holder.itemView.image_favorite.setOnClickListener {
            favoriteIconItem?.invoke(listOfLegoSet[position])
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

    fun swapItem(position: Int) {
        mRecentlySwipedItem = listOfLegoSet[position]
        swipedItem?.invoke(listOfLegoSet[position])
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
        fun bindView(item: LegoSet, favoritesLegoSets: MutableList<LegoSet>?) {
            item.isInFavorite = if (favoritesLegoSets?.find { legoSet -> legoSet.set_num == item.set_num } != null)
                true else false
            binding.legoSet = item
            binding.executePendingBindings()
        }
    }
}