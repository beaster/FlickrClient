package co.flickr.client.app.adapter


import android.content.Context
import android.view.ViewGroup
import co.flickr.client.R
import co.flickr.client.app.adapter.paging.Paginable
import co.flickr.client.app.adapter.paging.Paginable.AdapterState.FOOTER_LOADED
import co.flickr.client.app.adapter.paging.Paginable.AdapterState.LOAD_FOOTER
import co.flickr.client.app.adapter.paging.PaginableImpl


abstract class FooterLoadingAdapter<T>(private val paging: Paginable = PaginableImpl()) :
        BaseAdapter<T>(), PaginableImpl.ChangeStateDelegate, Paginable by paging {

    init {
        (paging as PaginableImpl).delegate = this
    }

    override fun getItemCount(): Int {
        val size = super.getItemCount()
        return if (hasFooterLoad) size + 1 else size
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasFooterLoad && position >= super.getItemCount()) VIEW_FOOTER_PROGRESS else VIEW_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_FOOTER_PROGRESS -> LoadingHolder(parent.context, parent)
            else -> throw IllegalStateException("Unknown type $viewType")
        }
    }

    fun showLoading() {
        state = LOAD_FOOTER
    }

    fun hideLoading() {
        state = FOOTER_LOADED
    }

    override fun onChangeState(newState: Paginable.AdapterState) {
        when (newState) {
            LOAD_FOOTER -> {
                if (!hasFooterLoad) {
                    hasFooterLoad = true
                    notifyItemInserted(itemCount)
                }
            }
            FOOTER_LOADED -> {
                if (hasFooterLoad) {
                    hasFooterLoad = false
                    notifyItemRemoved(itemCount)
                }
            }
            else -> {
                //Nothing
            }
        }
    }

    class LoadingHolder(context: Context, parent: ViewGroup) :
            BaseViewHolder(context, R.layout.layout_loading_item, parent)

    companion object {
        const val VIEW_ITEM = 0
        const val VIEW_FOOTER_PROGRESS = 1
    }

}