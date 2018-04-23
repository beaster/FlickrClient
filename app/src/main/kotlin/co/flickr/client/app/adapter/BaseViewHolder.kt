package co.flickr.client.app.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    constructor(context: Context, @LayoutRes layoutId: Int, parent: ViewGroup?) :
            this(LayoutInflater.from(context).inflate(layoutId, parent, false))

    open fun onViewRecycled() = Unit
}