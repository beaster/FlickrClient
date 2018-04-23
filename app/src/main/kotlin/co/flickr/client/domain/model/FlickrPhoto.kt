package co.flickr.client.domain.model

import co.flickr.client.util.FlickrUtil
import co.flickr.client.util.unsafeLazy

class FlickrPhoto(val id: String,
                  val owner: String,
                  val secret: String,
                  val server: String,
                  val farm: Int,
                  val title: String?,
                  val isPublic: Boolean,
                  val isFriend: Boolean,
                  val isFamily: Boolean) {

    val smallUrl by unsafeLazy { FlickrUtil.createUrl(this, PhotoSize.SMALL) }
    val mediumUrl by unsafeLazy { FlickrUtil.createUrl(this, PhotoSize.MEDIUM) }
    val largeUrl by unsafeLazy { FlickrUtil.createUrl(this, PhotoSize.LARGE) }

}