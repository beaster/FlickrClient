package co.flickr.client.app.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import co.flickr.client.R
import co.flickr.client.domain.model.FlickrPhoto
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class PhotoAdapter : FooterLoadingAdapter<FlickrPhoto>() {

    var listener: Listener? = null

    fun clearAll() {
        clear()
        notifyDataSetChanged()
    }

    fun addToBottom(photos: List<FlickrPhoto>) {
        val startPosition = data.size
        data.addAll(photos)
        notifyItemRangeInserted(startPosition, photos.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == VIEW_ITEM) {
            PhotoHolder(parent.context, parent)
        } else {
            super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is PhotoHolder) {
            holder.bind(getItem(position))
        }
    }

    inner class PhotoHolder(context: Context, parent: ViewGroup) :
            BaseViewHolder(context, R.layout.photo_item, parent) {

        private lateinit var item: FlickrPhoto
        private val photoView: ImageView = itemView.findViewById(R.id.photo)
        private val photoSize = itemView.context.resources.getDimensionPixelSize(R.dimen.image_size)

        init {
            itemView.setOnClickListener { listener?.onItemClickListener(item) }
        }

        fun bind(item: FlickrPhoto) {
            this.item = item

            Glide.with(itemView.context)
                    .asBitmap()
                    .apply(RequestOptions.overrideOf(photoSize, photoSize))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .apply(RequestOptions.noAnimation())
                    .apply(RequestOptions.noTransformation())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                    .load(item.smallUrl)
                    .into(photoView)
        }

        override fun onViewRecycled() {
            super.onViewRecycled()
            Glide.with(itemView.context).clear(photoView)
        }
    }

    interface Listener {
        fun onItemClickListener(item: FlickrPhoto)
    }

}