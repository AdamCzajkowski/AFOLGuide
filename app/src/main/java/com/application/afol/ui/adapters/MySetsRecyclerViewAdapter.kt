package com.application.afol.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.databinding.SingleFavoriteLegoSetBinding
import com.application.afol.models.LegoSet
import kotlinx.android.synthetic.main.single_favorite_lego_set.view.*

class MySetsRecyclerViewAdapter : RecyclerView.Adapter<MySetsRecyclerViewAdapter.ViewHolder>() {

    var listOfLegoSet = mutableListOf<LegoSet>()

    var selectedItem: ((LegoSet) -> Unit)? = null

    var deleteItem: ((LegoSet) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context.applicationContext)
        val binding = SingleFavoriteLegoSetBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bindView(listOfLegoSet[position])
            itemView.setOnClickListener {
                selectedItem?.invoke(listOfLegoSet[position])
            }
            itemView.delete_button.setOnClickListener {
                deleteItem?.invoke(listOfLegoSet[position])
            }
        }
    }

    override fun getItemCount() =
        listOfLegoSet.size

    fun removeItem(legoSet: LegoSet) {
        listOfLegoSet.remove(legoSet)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: SingleFavoriteLegoSetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: LegoSet) =
            with(binding) {
                legoSet = item
                executePendingBindings()
            }
    }
}
