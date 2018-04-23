package co.flickr.client.domain.interactor

import co.flickr.client.domain.model.FlickrPhoto
import co.flickr.client.domain.paging.PaginationState
import co.flickr.client.domain.repository.FlickrRepository
import co.flickr.client.domain.repository.SuggestionRepository
import co.flickr.client.util.unsafeLazy
import io.reactivex.Flowable
import io.reactivex.Observable

class PhotoSearchInteractor(private val flickrRepository: FlickrRepository,
                            private val suggestionRepository: SuggestionRepository) : IPhotoInteractor {

    private val paginator by unsafeLazy { PhotoFlickrPaginator(flickrRepository) }

    override fun loadNextPage() {
        paginator.loadNextPage()
    }

    override fun reload(text: String) {
        paginator.reload(text)
    }

    override fun observe(): Observable<PaginationState<FlickrPhoto, String>> {
        return paginator.observable()
    }

    override fun finish() {
        paginator.finish()
    }

    override fun addSuggestion(suggestion: String) {
        suggestionRepository.addSuggestion(suggestion).subscribe()
    }

    override fun observeSuggestions(): Flowable<List<String>> =
            suggestionRepository.getSuggestions()

}