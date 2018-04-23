package co.flickr.client.util

import co.flickr.client.domain.model.FlickrPhoto
import co.flickr.client.domain.model.PhotoSize


object FlickrUtil {

    fun createUrl(photo: FlickrPhoto, size: PhotoSize) =
            "https://farm${photo.farm}.staticflickr.com/${photo.server}/" +
                    "${photo.id}_${photo.secret}_${size.suffix}.jpg"

}