package co.flickr.client.domain.repository

import co.flickr.client.domain.model.PhotoContainer
import io.reactivex.Single

interface FlickrRepository {

    fun searchPhotosByText(text: String, page: Int): Single<PhotoContainer>

}