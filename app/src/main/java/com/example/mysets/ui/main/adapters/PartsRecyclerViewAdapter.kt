package com.example.mysets.ui.main.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SinglePartBinding
import com.example.mysets.models.PartResult
import kotlinx.android.synthetic.main.single_part.view.*

class PartsRecyclerViewAdapter : RecyclerView.Adapter<PartsRecyclerViewAdapter.ViewHolder>() {
    private var listOfParts = mutableListOf<PartResult.Result>()

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

    fun swapList(list: MutableList<PartResult.Result>) {
        listOfParts.clear()
        listOfParts.addAll(list)
        notifyDataSetChanged()
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
            adapter.bindImageToUrl = { url ->
                val openURLFromImage = Intent(Intent.ACTION_VIEW)
                openURLFromImage.data = Uri.parse(url)
                startActivity(context, openURLFromImage, Bundle())
            }
        }
    }
}