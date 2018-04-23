package co.flickr.client.app.adapter

import android.support.v7.widget.RecyclerView


abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {

    protected val data = mutableListOf<T>()

    override fun getItemCount() = data.size

    fun getItem(position: Int) = data[position]
    fun isEmpty() = data.size == 0
    fun clear() = data.clear()

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}