package co.flickr.client.data.model.network

import com.google.gson.annotations.SerializedName

class NWPhotoContainer {

    @SerializedName("page")
    var page: Int? = null

    @SerializedName("pages")
    var pages: Int? = null

    @SerializedName("perpage")
    var perpage: Int? = null

    @SerializedName("total")
    var total: Int? = null

    @SerializedName("photo")
    var photos: List<NWPhoto>? = null
}