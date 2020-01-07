package com.application.afol.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.databinding.SinglePartBinding
import com.application.afol.models.PartResult
import kotlinx.android.synthetic.main.single_part.view.*

private const val MULTIPLIER = 0.1

class PartsRecyclerViewAdapter : RecyclerView.Adapter<PartsRecyclerViewAdapter.ViewHolder>() {
    var listOfParts = mutableListOf<PartResult.Result>()

    var partSelectedURL: ((String) -> Unit)? = null

    var endMarker: ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SinglePartBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfParts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bindView(listOfParts[position], position + 1)
            itemView.imageView.setOnClickListener {
                partSelectedURL?.invoke(listOfParts[position].partUrl)
            }
        }

        if (position == itemCount - (MULTIPLIER * itemCount).toInt()) endMarker?.invoke(true)
        else endMarker?.invoke(false)
    }

    fun addToList(list: MutableList<PartResult.Result>) {
        listOfParts.addAll(list)
        notifyItemRangeChanged(itemCount - list.size, itemCount)
    }

    fun clearList() {
        listOfParts.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SinglePartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: PartResult.Result, position: Int) {
            binding.part = item
            itemView.position_value.text = position.toString()
        }
    }
}