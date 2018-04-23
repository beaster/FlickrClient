package co.flickr.client.app.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import co.flickr.client.domain.interactor.PhotoSearchInteractor
import co.flickr.client.domain.model.FlickrPhoto
import co.flickr.client.domain.paging.PaginationState
import co.flickr.client.util.toLiveData
import co.flickr.client.util.unsafeLazy
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class FlickrViewModel(private val photoInteractor: PhotoSearchInteractor) : ViewModel() {

    private val textSubject = PublishSubject.create<String>()
    private var lastQuery = ""

    val reloadProgressLiveData = SingleLiveEvent<Boolean>()
    val suggestionsLiveData: LiveData<List<String>> by unsafeLazy {
        photoInteractor.observeSuggestions()
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .toLiveData()
    }

    val photoLiveData: LiveData<PaginationState<FlickrPhoto, String>> by unsafeLazy {
        photoInteractor.observe()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.LATEST)
                .distinctUntilChanged()
                .doOnNext { state ->
                    if (state.reloaded) {
                        reloadProgressLiveData.value = false
                    }
                }
                .doOnCancel { reloadProgressLiveData.value = false }
                .toLiveData()
    }

    init {
        textSubject
                .debounce(DEBOUNCE, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() && it != lastQuery }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { query -> startReload(query) }
                .subscribe()
    }

    private fun startReload(query: String) {
        lastQuery = query
        reloadProgressLiveData.value = true
        photoInteractor.reload(query)
    }

    fun reload() {
        startReload(lastQuery)
    }

    fun loadNext() {
        photoInteractor.loadNextPage()
    }

    fun onQueryTextChange(text: String) {
        textSubject.onNext(text)
    }

    fun addSuggestion(suggestion: String) {
        photoInteractor.addSuggestion(suggestion)
    }

    fun onSuggestionClick(suggestion: String) {
        onQueryTextChange(suggestion)
    }

    companion object {
        private const val DEBOUNCE = 300L
    }

}