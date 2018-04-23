package co.flickr.client.domain.interactor

import co.flickr.client.domain.model.FlickrPhoto
import co.flickr.client.domain.model.PhotoContainer
import co.flickr.client.domain.paging.PaginationState
import co.flickr.client.domain.paging.Paginator
import co.flickr.client.domain.repository.FlickrRepository
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers


class PhotoFlickrPaginator(private val repository: FlickrRepository) : Paginator<FlickrPhoto, String> {

    private var page = DEFAULT_OFFSET_PAGE
    private var condition = ""

    private var emitter: ObservableEmitter<PaginationState<FlickrPhoto, String>>? = null
    private var lastDisposable: Disposable? = null

    override fun loadPrevPage() {}

    override fun loadNextPage() {
        val nextPage = page + 1
        lastDisposable?.dispose()
        lastDisposable = emitter?.invokeAction(
                remote = { repository.searchPhotosByText(condition, nextPage) },
                mapper = { container ->
                    if (container.isSuccess) {
                        page++
                    }
                    resultState(container, false)
                },
                errorMapper = { errorState(false) }
        )
    }

    override fun reload(condition: String) {
        this.condition = condition
        page = DEFAULT_OFFSET_PAGE
        lastDisposable?.dispose()
        lastDisposable = emitter?.invokeAction(
                remote = { repository.searchPhotosByText(condition, page) },
                mapper = { resultState(it, true) },
                errorMapper = { errorState(true) }
        )
    }

    private fun resultState(container: PhotoContainer, reload: Boolean) =
            PaginationState(
                    reloaded = reload,
                    data = container.data,
                    reloadCondition = condition,
                    allLoadedEnd = container.totalPages <= page,
                    errorMessage = container.errorMessage,
                    hasLoadingErrorEnd = !container.isSuccess,
                    offset = page)

    private fun errorState(reload: Boolean) =
            PaginationState<FlickrPhoto, String>(
                    reloaded = reload,
                    hasLoadingErrorEnd = true,
                    reloadCondition = condition,
                    offset = page)

    override fun observable(): Observable<PaginationState<FlickrPhoto, String>> {
        return createObservable { emitter = it }
    }

    override fun finish() {
        lastDisposable?.dispose()
        emitter = null
    }

    private fun <T> createObservable(callback: (emitter: ObservableEmitter<T>) -> Unit): Observable<T> {
        val subscriber = ObservableOnSubscribe<T> { emitter -> callback.invoke(emitter) }
        return Observable.create(subscriber)
    }

    private fun <R, L, C> ObservableEmitter<PaginationState<L, C>>.invokeAction(remote: () -> Single<R>,
                                                                                mapper: (R) -> PaginationState<L, C>,
                                                                                errorMapper: (Throwable) -> PaginationState<L, C>): Disposable {
        val remoteDisposable = remote.invoke()
                .map(mapper)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe({ data ->
                    if (!this.isDisposed) {
                        this.onNext(data)
                    }
                }, { error ->
                    if (!this.isDisposed) {
                        this.onNext(errorMapper.invoke(error))
                    }
                })

        if (!this.isDisposed) {
            this.setDisposable(Disposables.fromAction {
                if (!remoteDisposable.isDisposed) {
                    remoteDisposable.dispose()
                }
            })
        }

        return remoteDisposable
    }

    companion object {
        private const val DEFAULT_OFFSET_PAGE = 1
    }
}