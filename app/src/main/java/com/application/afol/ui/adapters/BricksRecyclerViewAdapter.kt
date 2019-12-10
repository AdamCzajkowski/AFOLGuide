package com.application.afol.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.databinding.SingleBrickRowBinding
import com.application.afol.models.BrickResult
import kotlinx.android.synthetic.main.single_brick_row.view.*

class BricksRecyclerViewAdapter : RecyclerView.Adapter<BricksRecyclerViewAdapter.ViewHolder>() {
    private var listOfBricks = mutableListOf<BrickResult.Result>()

    var endMarker: ((Boolean) -> Unit)? = null

    var brickSelected: ((String) -> Unit)? = null

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SingleBrickRowBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount() = listOfBricks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bindView(listOfBricks[position], position + 1)
            itemView.imageView.setOnClickListener { brickSelected?.invoke(listOfBricks[position].part.partUrl) }
        }
        if (position == itemCount - (0.1 * itemCount).toInt()) endMarker?.invoke(true)
        else endMarker?.invoke(false)
    }

    fun swapList(list: MutableList<BrickResult.Result>) =
        with(listOfBricks) {
            clear()
            addAll(list)
        }.also { notifyDataSetChanged() }

    fun addToList(list: MutableList<BrickResult.Result>) =
        listOfBricks.addAll(list).also { notifyItemRangeChanged(itemCount - list.size, itemCount) }

    fun clearList() {
        listOfBricks.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SingleBrickRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: BrickResult.Result, position: Int) {
            with(binding) {
                brickColor = item.color
                brickDetail = item.part
                brickQuantity = item
            }
            itemView.position_value.text = position.toString()
        }

        /*fun createAdapter(context: Context) {
            val adapter = BindingAdapter
            binding.adapter = adapter
            BindingAdapter.bindURLParse = { url ->
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(url)
                startActivity(context, openURL, Bundle())
            }
            BindingAdapter.bindImageToUrl = { url ->
                val openURLFromImage = Intent(Intent.ACTION_VIEW)
                openURLFromImage.data = Uri.parse(url)
                startActivity(context, openURLFromImage, Bundle())
            }
        }*/
    }
}