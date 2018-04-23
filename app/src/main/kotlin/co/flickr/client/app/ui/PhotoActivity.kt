package co.flickr.client.app.ui

import android.arch.lifecycle.Observer
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.Toast
import co.flickr.client.R
import co.flickr.client.app.adapter.FooterLoadingAdapter
import co.flickr.client.app.adapter.PhotoAdapter
import co.flickr.client.app.adapter.paging.EndlessScrollListener
import co.flickr.client.app.presentation.FlickrViewModel
import co.flickr.client.domain.model.FlickrPhoto
import co.flickr.client.domain.paging.PaginationState
import co.flickr.client.util.getViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.util.FixedPreloadSizeProvider
import kotlinx.android.synthetic.main.activity_photo.*


class PhotoActivity : AppCompatActivity() {

    private lateinit var flickrModel: FlickrViewModel
    private lateinit var adapter: PhotoAdapter
    private lateinit var scrollListener: EndlessScrollListener
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        setupView()
        setupModel()
    }

    private fun setupView() {
        setupRecyclerView()
        swipeRefresh.setOnRefreshListener { flickrModel.reload() }
    }

    private fun setupRecyclerView() {
        val gridMargin = resources.getDimensionPixelOffset(R.dimen.grid_margin)
        val photoSize = resources.getDimensionPixelOffset(R.dimen.image_size)
        val spanCount = resources.displayMetrics.widthPixels / (photoSize + 2 * gridMargin)

        val layoutManager = GridLayoutManager(this, spanCount)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                        state: RecyclerView.State) {
                outRect.set(gridMargin, gridMargin, gridMargin, gridMargin)
            }
        })

        val heightCount = resources.displayMetrics.heightPixels / photoSize
        recyclerView.recycledViewPool.setMaxRecycledViews(0, spanCount * heightCount * 2)
        recyclerView.setItemViewCacheSize(0)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    FooterLoadingAdapter.VIEW_ITEM -> 1
                    FooterLoadingAdapter.VIEW_FOOTER_PROGRESS -> spanCount
                    else -> -1
                }
            }
        }

        //endless scroll
        scrollListener = object : EndlessScrollListener(layoutManager) {
            override fun onLoadDataToFooter() {
                super.onLoadDataToFooter()
                adapter.showLoading()
                flickrModel.loadNext()
            }
        }
        recyclerView.addOnScrollListener(scrollListener)

        //image preloader
        recyclerView.addOnScrollListener(buildPreloader(photoSize))

        adapter = PhotoAdapter()
        adapter.listener = object : PhotoAdapter.Listener {
            override fun onItemClickListener(item: FlickrPhoto) {
                openDetails(item)
            }
        }
        recyclerView.adapter = adapter
    }

    private fun buildPreloader(photoSize: Int): RecyclerViewPreloader<FlickrPhoto> {
        val preloadSizeProvider = FixedPreloadSizeProvider<FlickrPhoto>(photoSize, photoSize)
        val preloadModelProvider = object : ListPreloader.PreloadModelProvider<FlickrPhoto> {
            override fun getPreloadItems(position: Int): List<FlickrPhoto> {
                return when (adapter.getItemViewType(position)) {
                    FooterLoadingAdapter.VIEW_ITEM -> listOf(adapter.getItem(position))
                    else -> emptyList()
                }
            }

            override fun getPreloadRequestBuilder(item: FlickrPhoto): RequestBuilder<*>? {
                return Glide.with(this@PhotoActivity)
                        .load(item.smallUrl)
                        .apply(RequestOptions.overrideOf(photoSize, photoSize))
            }
        }

        return RecyclerViewPreloader<FlickrPhoto>(this, preloadModelProvider,
                preloadSizeProvider, MAX_PHOTO_PRELOAD)
    }

    private fun setupModel() {
        flickrModel = getViewModel()
        flickrModel.photoLiveData.observe(this, Observer { update(it!!) })
        flickrModel.reloadProgressLiveData.observe(this, Observer { updateProgress(it!!) })
        flickrModel.suggestionsLiveData.observe(this, Observer { updateSuggestion(it!!) })
    }

    private fun update(state: PaginationState<FlickrPhoto, String>) {
        if (state.hasLoadingErrorEnd) {
            if (state.reloaded) {
                val msg = state.errorMessage ?: resources.getText(R.string.error_no_network)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        } else {
            adapter.addToBottom(state.data)
            scrollListener.moreFooterDataAvailable = !state.allLoadedEnd
            adapter.hideLoading()
        }
        scrollListener.markFooterLoaded()
    }

    private fun updateProgress(showProgress: Boolean) {
        swipeRefresh.isRefreshing = showProgress
        if (showProgress) {
            scrollListener.resetState()
            adapter.hideLoading()
            adapter.clearAll()
        } else {
            scrollListener.markFooterLoaded()
        }
    }

    private fun openDetails(item: FlickrPhoto) {
        PreviewActivity.start(this, item.smallUrl)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_photo_search, menu)

        val menuItem = menu.findItem(R.id.menu_search)
        searchView = menuItem.actionView as SearchView

        menuItem.expandActionView()
        setupSearchView(searchView) {
            menuItem.collapseActionView()
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSearchView(searchView: SearchView, submitCallback: () -> Unit) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                flickrModel.addSuggestion(query)
                submitCallback.invoke()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                flickrModel.onQueryTextChange(newText)
                return true
            }

        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                if (cursor.moveToPosition(position)) {
                    val query = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT))
                    searchView.setQuery(query, true)
                    flickrModel.onSuggestionClick(query)
                }
                return true
            }
        })
    }

    private fun updateSuggestion(suggestion: List<String>) {
        val cursor = buildCursor(suggestion)
        val suggestionAdapter = SimpleCursorAdapter(this, R.layout.layout_suggestion_item, cursor,
                arrayOf(COLUMN_TEXT), intArrayOf(R.id.text), 0)
        suggestionAdapter.setFilterQueryProvider { query ->
            buildCursor(suggestion.filter { it.startsWith(query) })
        }
        searchView.suggestionsAdapter = suggestionAdapter
    }

    private fun buildCursor(suggestions: List<String>): MatrixCursor {
        val cursor = MatrixCursor(arrayOf(COLUMN_ID, COLUMN_TEXT))
        suggestions.forEachIndexed { index, suggestion ->
            cursor.newRow().add(COLUMN_ID, index)
                    .add(COLUMN_TEXT, suggestion)
        }
        return cursor
    }

    companion object {
        private const val MAX_PHOTO_PRELOAD = 70

        private const val COLUMN_ID = "_id"
        private const val COLUMN_TEXT = "text"

    }
}