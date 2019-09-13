package com.application.afol.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.databinding.SinglePartBinding
import com.application.afol.models.PartResult
import com.application.afol.utility.autoNotify
import kotlinx.android.synthetic.main.single_part.view.*
import kotlin.properties.Delegates

class PartsRecyclerViewAdapter : RecyclerView.Adapter<PartsRecyclerViewAdapter.ViewHolder>() {
    var listOfParts: MutableList<PartResult.Result> by Delegates.observable(mutableListOf()) { _, oldList, newList ->
        autoNotify(oldList, newList) { old, new -> old.partCatId == new.partCatId }
    }

    var endMarker: ((Boolean) -> Unit)? = null

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SinglePartBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfParts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfParts[position], position + 1)
        holder.createAdapter(context)
        if (position == itemCount - (0.1 * itemCount).toInt()) {
            endMarker?.invoke(true)
        } else {
            endMarker?.invoke(false)
        }
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

        fun createAdapter(context: Context) {
            val adapter = BindingAdapter
            binding.adapter = adapter
            BindingAdapter.bindImageToUrl = { url ->
                val openURLFromImage = Intent(Intent.ACTION_VIEW)
                openURLFromImage.data = Uri.parse(url)
                startActivity(context, openURLFromImage, Bundle())
            }
        }
    }
}