package co.flickr.client.data.model.converter

import co.flickr.client.data.model.network.NWContainer
import co.flickr.client.domain.model.PhotoContainer

object PhotoContainerConverter : SourceConverter<NWContainer, PhotoContainer> {

    override fun fromNetwork(source: NWContainer): PhotoContainer {

        val page = getOrZero(source.photoContainer?.page)
        val pages = getOrZero(source.photoContainer?.pages)
        val photoList = source.photoContainer?.photos?.map { PhotoConverter.fromNetwork(it) }
                ?: emptyList()

        return PhotoContainer(page, pages, photoList).apply {
            errorCode = source.code
            errorMessage = source.message
            isSuccess = source.stat == "ok"
        }
    }
}