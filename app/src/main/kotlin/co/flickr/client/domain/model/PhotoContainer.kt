package co.flickr.client.domain.model

class PhotoContainer(val page: Int,
                     val totalPages: Int,
                     val data: List<FlickrPhoto>) {
    var errorCode: Int? = null
    var errorMessage: String? = null
    var isSuccess: Boolean = false

}