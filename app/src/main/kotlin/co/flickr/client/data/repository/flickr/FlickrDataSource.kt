package co.flickr.client.data.repository.flickr

import co.flickr.client.data.model.converter.PhotoContainerConverter
import co.flickr.client.data.model.network.NWContainer
import co.flickr.client.data.network.Constants
import co.flickr.client.data.network.FlickrService
import co.flickr.client.data.network.NetworkClient
import co.flickr.client.domain.model.PhotoContainer
import co.flickr.client.util.unsafeLazy
import io.reactivex.Single

class FlickrDataSource {

    class RemoteSource constructor(private val api: FlickrService,
                                   private val mapper: (NWContainer) -> PhotoContainer) : DataSource {

        override fun searchPhotoByText(text: String, page: Int): Single<PhotoContainer> =
                api.searchPhotoByText(Constants.FLICKR_API_KEY, text, page, Constants.PER_PAGE)
                        .map(mapper)

        companion object {
            val instance by unsafeLazy {
                RemoteSource(NetworkClient.createFlickrService(), { PhotoContainerConverter.fromNetwork(it) })
            }
        }
    }

    interface DataSource {
        fun searchPhotoByText(text: String, page: Int): Single<PhotoContainer>
    }

    class Factory(val remote: DataSource)
}