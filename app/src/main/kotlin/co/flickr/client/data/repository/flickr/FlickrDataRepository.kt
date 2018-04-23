package co.flickr.client.data.repository.flickr

import co.flickr.client.domain.repository.FlickrRepository

class FlickrDataRepository(factory: FlickrDataSource.Factory) : FlickrRepository {

    private val remote = factory.remote

    override fun searchPhotosByText(text: String, page: Int) =
            remote.searchPhotoByText(text, page)

}