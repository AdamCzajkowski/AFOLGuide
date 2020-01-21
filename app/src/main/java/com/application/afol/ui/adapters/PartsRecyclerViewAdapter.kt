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
import kotlinx.android.synthetic.main.single_part.view.*

private const val MULTIPLIER = 0.1

class PartsRecyclerViewAdapter : RecyclerView.Adapter<PartsRecyclerViewAdapter.ViewHolder>() {
    var listOfParts = mutableListOf<PartResult.Result>()

    var partSelectedURL: ((String) -> Unit)? = null

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
        with(holder) {
            bindView(listOfParts[position])
            createAdapter(context)
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
        fun bindView(item: PartResult.Result) {
            binding.part = item
        }

        fun createAdapter(context: Context) {
            val adapter = BindingAdapter
            binding.adapter = adapter

            BindingAdapter.bindBLPart = { url ->
                val openURLFromBL = Intent(Intent.ACTION_VIEW)
                println("----------------------------> $url")
                "https://www.bricklink.com/v2/search.page?q=0902&brand=1000&type=P&tab=A#T=A"
                openURLFromBL.data =
                    Uri.parse("https://www.bricklink.com/v2/search.page?q=$url&brand=1000&type=P&tab=A#T=A")
                startActivity(context, openURLFromBL, Bundle())
            }
        }
    }
}