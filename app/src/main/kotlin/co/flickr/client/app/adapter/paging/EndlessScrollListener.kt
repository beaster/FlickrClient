package co.flickr.client.app.adapter.paging

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessScrollListener(private var layoutManager: RecyclerView.LayoutManager) :
        RecyclerView.OnScrollListener() {

    private var loadingHeader = false
    private var loadingFooter = false

    var moreFooterDataAvailable = false
    var moreHeaderDataAvailable = false

    final override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val llm = layoutManager as LinearLayoutManager
        val totalItemCount = layoutManager.itemCount

        val firstVisibleItemPosition = llm.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = llm.findLastVisibleItemPosition()

        if (!loadingHeader && moreHeaderDataAvailable && firstVisibleItemPosition - VISIBLE_THRESHOLD < 0) {
            onLoadDataToHeader()
            loadingHeader = true
        }

        if (!loadingFooter && moreFooterDataAvailable && lastVisibleItemPosition + VISIBLE_THRESHOLD > totalItemCount) {
            onLoadDataToFooter()
            loadingFooter = true
        }

    }

    fun markHeaderLoaded() {
        loadingHeader = false
    }

    fun markFooterLoaded() {
        loadingFooter = false
    }

    fun resetState() {
        moreFooterDataAvailable = false
        moreHeaderDataAvailable = false
    }

    protected open fun onLoadDataToHeader() = Unit
    protected open fun onLoadDataToFooter() = Unit

    companion object {
        private const val VISIBLE_THRESHOLD = 20
    }
}