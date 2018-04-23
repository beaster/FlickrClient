package co.flickr.client.domain.interactor

import co.flickr.client.domain.model.FlickrPhoto
import co.flickr.client.domain.paging.PaginationState
import io.reactivex.Flowable
import io.reactivex.Observable

interface IPhotoInteractor {
    fun loadNextPage()
    fun reload(text: String)
    fun observe(): Observable<PaginationState<FlickrPhoto, String>>
    fun addSuggestion(suggestion: String)
    fun observeSuggestions(): Flowable<List<String>>
    fun finish()
}
